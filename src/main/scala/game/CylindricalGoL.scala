// Zack Ross
// zmr462
// 11215196

package game


/**
  * This class represents a single game of Conway's Game of Life.
  * In this version, horizontal (column) cell wrapping is allowed.
  */
class CylindricalGoL extends GoL {
  /**
  * Standardize a given cell location to a location within the state. This
  * function defines the cell wrapping behavior.
  *
  * @param loc the cell location
  * @return the cell location if it in the state, None otherwise
  */
  protected def standardize(loc: (Int, Int)): Option[(Int, Int)] = {
    if (state.contains(loc)) 
        Some(loc)
    if (0 <= loc._1 && loc._1 < rows) {
      if (loc._2 < 0)
        Some((loc._1, cols - 1))
      if (loc._2 >= cols)
        Some((loc._1, 0))
    }
    None
  }
}