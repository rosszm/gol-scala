// Zack Ross
// zmr462
// 11215196

package game


/**
  * This class represents a single game of Conway's Game of Life.
  * In this version, horizontal and vertical cell wrapping is allowed.
  */
class ToroidalGoL extends GoL {
    /**
    * Standardize a given cell location to a location within the state. This
    * function defines the cell wrapping behavior.
    *
    * @param loc the cell location
    * @return the cell location if it in the state, None otherwise
    */
  protected def standardize(loc: (Int, Int)): Option[(Int, Int)] = {
    if (state.contains(loc)) Some(loc)
    var row, col = 0
    // horizontal
    if (loc._2 < 0)
      col = cols - 1
    else if (loc._2 >= cols)
      col = 0
    // vertical
    if (loc._1 < 0)
      row = rows - 1
    else if (loc._1 >= rows)
      row = 0
    if (state.contains((row, col))) Some((row, col)) else None
    
  }
}