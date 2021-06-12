// Zack Ross
// zmr462
// 11215196

package game.aspects

import java.io.File
import java.io.FileWriter
import java.io.BufferedWriter

/**
  * Logs the cell population every tick.
  */
trait PopulationLogger extends TickMonitor {
  // create an new population.out file
  val file = new File("./population.out")
  

  // create the writer to append tick populations to the file
  val writer = new BufferedWriter(new FileWriter(file, false))

  
  /**
    * Update every cell in game. Cell updates are performed in parallel.
    * 
    * This overrides the tick method to log the old and new populations after
    * a given tick is performed.
    */
  abstract override def tick() {
    val oldPop = super.population()
    super.tick()
    val newPop = super.population()
    writer.write(s"${oldPop}, ${newPop}\n")
    writer.flush()
  }
}