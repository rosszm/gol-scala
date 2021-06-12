// Zack Ross
// zmr462
// 11215196

package game


/**
  * This class represents a single game of Conway's Game of Life.
  * In this version no cell wrapping is allowed.
  */
class FlatGoL extends GoL {
    /**
    * Standardize a given cell location to a location within the state. This
    * function defines the cell wrapping behavior.
    *
    * @param loc the cell location
    * @return the cell location if it in the state, None otherwise
    */
  protected def standardize(loc: (Int, Int)): Option[(Int, Int)] = {
    if (state.contains(loc)) Some(loc) else None
  }
}