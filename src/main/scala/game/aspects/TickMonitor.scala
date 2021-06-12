// Zack Ross
// zmr462
// 11215196

package game.aspects
import game.GoL


/**
  * `TickMonitor` Trait
  */
trait TickMonitor extends GoL {
  
  /**
    * Update every cell in game. Cell updates are performed in parallel.
    */
  def tick()
}