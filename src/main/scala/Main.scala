// Zack Ross
// zmr462
// 11215196

import game.GoL


object Main extends App {
  if (args.length == 0) {
    Console.err.println(s"Missing Arguments")
    sys.exit(0)
  }
  val program = args(0)

  // usage instructions
  val usage = s"""Usage:
    |  $program [-h][-t <ticks>][-g <geo>] <input file> <output file>
    |Options:
    |  -h            displays the usage
    |  -t <ticks>    runs gol for the given number of ticks
    |  -g <geo>    sets the geometry type""".stripMargin

  // handle program arguments
  var argv = Vector[String]()
  var ticks = 100000 // default number of ticks
  var geometry = "f"
  handleArgs(args)

  val input: String = argv(0)
  val output: String = argv(1)
  
  // create game
  val gol = GoL(input, rules)(geometry.toLowerCase()) match {
    case Some(g) => g
    case None => {
      Console.err.println(s"Cannot read input file")
      sys.exit(1)
    }
  }
  // run for given number of iterations and write
  (0 until ticks).foreach(t => gol.tick())
  gol.write(output)

  // Main Program Ends //

  
  /**
    * Handles the arguments and options provided to the program
    */
  def handleArgs(args: Array[String]) = {
    var arg = 0
    while (arg < args.length) {
      if (args(arg).startsWith("-")) 
        args(arg) match {
          case "-h" => {
            println(usage) // print the usage message
            sys.exit(0)
          }
          case "-t" => {
            ticks = parseTicks(args(arg + 1))
            arg += 1
          }
          case "-g" => {
            geometry = args(arg + 1)
            arg += 1
          }
        } 
      else {
        argv = argv :+ args(arg)
      }
      arg += 1
    }
  }

  /**
    * Parse the ticks argument.
    *
    * @param arg the arg string
    * @return the ticks
    */
  def parseTicks(arg: String): Int = {
    try { 
      arg.toInt
    } catch {
      case e: Exception => {
        Console.err.println(s"Invalid argument for -t\n${usage}")
        sys.exit(1)
      }
    }
  }

   /**
  * Update rule function for Game of Life. 
  * Takes the current state of a cell and the number of its living neighbours,
  * then returns the next state of the given cell.
  *
  * @param cs the current cell state
  * @param n the number of living neighbours
  * @return the next cell state
  */
  def rules(cs: Int, n: Int): Int = {
    n match {
      case 3 => 1
      case 2 => if (cs == 1) 1 else 0
      case _ => 0
    }
  }

}