package mud

import io.StdIn._
import mud.Room
import mud.Player

object Main {
  def main(args:Array[String]):Unit = {
   println("Hello, welcome to The Library. Available commands: get, add, list, move, look, quit, help.")
   var You = new Player(List(null), Room.rooms("MainFloor"))
   var input = ""
   while (input != "quit") {
     input = readLine()
     You.processCommand(input)
   }
  }
}