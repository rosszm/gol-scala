// Zack Ross
// zmr462
// 11215196

package game
import scala.io.{Source, BufferedSource}
import java.io._ 
import scala.collection.parallel.immutable.ParMap
import game.aspects.PopulationLogger


/**
  * This class represents a single game of Conway's Game of Life
  */
abstract class GoL {
  /** the number of rows */
  protected var rows: Int = 0
  /** the number of columns */
  protected var cols: Int = 0
  /** the cells */
  protected var state: ParMap[(Int, Int), Int] = ParMap()

  protected var updated: (Int, Int) => Int = rules

   /**
  * Default Rule function: returns the same state
  *
  * @param cs the current cell state
  * @param n the number of living neighbours
  * @return the next cell state
  */
  def rules(cs: Int, n: Int): Int = cs

  /**
    * Write the current game state to the file at the given path.
    *
    * @param path the file path
    */
  def write(path: String) {
    val file = new File(path)
    val writer = new BufferedWriter(new FileWriter(file))
    writer.write(s"${cols} ${rows}\n") // write first line of file
    // sort the cells by row and column to be written
    val finalState = state.toVector.sortBy(_._1)
    var col = 0
    var line: StringBuilder = new StringBuilder()
    for ((loc, cellState) <- finalState) {
      if (loc._2 == cols - 1) writer.write("\n")
      writer.write(cellToString(cellState))
    }
    writer.close()
  }

  /**
    * Update every cell in game. Cell updates are performed in parallel.
    * Returns the old and new populations
    * @return the populations
    */
  def tick() {    
    state = state.par.map(cell => {
      val n = aliveNeighbours(cell._1)
      (cell._1, updated(cell._2, n))
    })

  }

  /**
    * Returns the current population of the game. Population is counted using
    * parallel map and count.
    *
    * @return the population.
    */
  def population(): Int = {
    state.par.map(cell => cell._2).count(c => c == 1)
  }

  /**
    * Returns the string representation of a cell.
    *
    * @param cellState the state of the cell
    * @return the cell as a string
    */
  protected def cellToString(cellState: Int): String = {
    cellState match {
      case 1 => "*"
      case _ => " "
    }
  }

  /**
    * Return the number of living cells that are neighbours to a given cell.
    *
    * @param loc the cell location
    * @return the number of living neighbours
    * @throws IllegalArgumentException if the cell is outside the state
    */
  def aliveNeighbours(loc: (Int, Int)): Int = {
    require(state.contains(loc))
    neighbourStates(loc).count(n => n == 1)
  }

  /**
    * get the states of all the neighbours of a cell.
    *
    * @param loc the cell location
    * @return the states of the neighbours
    * @throws IllegalArgumentException if the cell is outside the state
    */
  protected def neighbourStates(loc: (Int, Int)): Vector[Int] = {
    require(state.contains(loc))
    var neighbours: Vector[Int] = Vector()
    (-1 to 1).foreach(row => {
      (-1 to 1).foreach(col => {
        if (!(row == 0 && col == 0)) {
          standardize((loc._1 + row, loc._2 + col)) match {
            case Some(neighbour) => neighbours = neighbours :+ state(neighbour)
            case None =>
          }
        }
      })
    })
    neighbours
  }

  /**
    * Standardize a given cell location to a location within the state. This
    * function defines the cell wrapping behavior.
    *
    * @param loc the cell location
    * @return the cell location if it in the state, None otherwise
    */
  protected def standardize(loc: (Int, Int)): Option[(Int, Int)]

}

/**
  * Game of Life constructor object 
  */
object GoL {
  /**
    * Create a new Game of Life from a given file.
    *
    * @param path the path name of a file
    * @param f the update rule function
    * @param gameType the wrap type
    * @return the game if it can be read from the file, None otherwise
    */
  def apply(path: String, f: (Int, Int)=>Int)(gameType: String): Option[GoL] = {
    val game = gameType match {
      case "f" => new FlatGoL() with PopulationLogger
      case "c" => new CylindricalGoL() with PopulationLogger
      case "t" => new ToroidalGoL() with PopulationLogger
      case _ => return None
    }
    game.updated = f
    val src = try {
      Source.fromFile(path)
    } catch {
      case e: Exception => return None
    }
    val lines = src.getLines()
    if (!lines.hasNext) None // if file is empty
    val dim = parseDimensions(lines.next) match {
      case Some(dim) => {
        game.rows = dim(0)
        game.cols = dim(1)
      }
      case None => None
    }
    var row = 0 // row index
    while(lines.hasNext) { 
      var col = 0 // column index
      game.state = game.state ++ lines.next.map(c => {
        col += 1
        (row, col) -> (if (c == '*') 1 else 0)
      })
      assert(col == game.cols) // rows should contain the correct number of columns
      row += 1
    }
    assert(row == game.rows) // rows should contain the correct number of columns
    src.close()
    Some(game)
  }

  /**
    * Read the dimensions from the first line of the file. The first line should
    * have the following format: "<rows> <cols>"
    *
    * @param source the source file
    * @return the dimensions
    */
  protected def parseDimensions(line: String): Option[Array[Int]] = {
    Some(line.split(" ").map(d => {
      try {
        d.toInt
      } catch {
        case e: Exception => return None
      }
    }))
  }
}