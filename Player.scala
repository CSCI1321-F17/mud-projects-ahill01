package MUD
import io.StdIn._
class Player(private var inventory: List[Item], private var blueDot: Room) {
  import Room._
  /**
   * parses and processes command provided by user
   * possible commands = get item from inventory, add item to inventory, list valid commands, move, look, and quit
   * @param string input by user
   * @return boolean- true if valid command, false if invalid command
   */

  def processCommand(command: String): Unit = {
    if (command.contains("get")) {
      val command1 = command.stripPrefix("get ")
      getFromInventory(command1)
    }
    if (command.contains("add")) {
      val command1 = command.stripPrefix("add ")
     blueDot.ItemsList.find(_.name==command1) match {
        case Some(item) => addToInventory(item)
        case None => println("That item is not in this room")
      }
       
    }
    if (command.contains("list")) {
      println(inventoryListing(inventory))
    }
   
    if (command.contains("north")) {
      move("north")
      }
    else if (command.contains("south")) {
      move("south")
      }
    else if (command.contains("east")) {
    move("east")  
    }
    else if (command.contains("west")) {
    move("west")
    }
    else if (command.contains("up")) {
      move("up")
    }
    else if (command.contains("down")) {
      move("down")
    }
      
    if (command.contains("look")) {
      look()
    }
    if (command == "help") {
      help()
    }
    if (command.contains("quit")) {
      println("Bye!")
    }
  }

  /**
   * Gets item from inventory if possible and returns it
   * @return Item/true if item is in inventory, false if not
   */
  def getFromInventory(itemName: String): Boolean = {
    val index = inventory.indexOf("itemName")
    if (inventory.contains(itemName)) {
      inventory(index)
      true
    } else false

  }

  /**
   * Adds item to inventory
   * @return Unit
   */

  def addToInventory(item: Item): Unit = { inventory = inventory :+ (item) }

  /**
   * builds a string w/ contents of inventory for printing
   * @param List[Item]
   * @return string of inventory list
   */
  def inventoryListing(I: List[Item]): String = {
    val listing = inventory.mkString
    listing
  }

  /**
   *
   * @param string (north, south, east, west, up, down)
   * @return boolean- true if player can move, false if player cannot
   */
  def move(dir:String): Boolean = {
    val exitTest = rooms.getOrElse(dir,"none")
    if(exitTest == "none") false
     else true
  }
  /**
   * re-prints description of room
   */
  def look(): Unit = {
    println(blueDot.description())
   
  }

  /**
   * prints out options/formats for valid commands
   */
  def help(): Unit = {
    println("Command options")
    println("get + [item name]- gets item from your inventory")
    println("add + [item name]- adds item to your inventory")
    println("list- lists items currently in your inventory")
    println("to move type north, south, east, west, up, or down")
    println("look- reprints room description")
  }
}
/**
 * Companion object for class Player
 *
 */
object Player {

}