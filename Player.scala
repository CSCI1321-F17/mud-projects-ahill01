package MUD
import io.StdIn._
class Player (private var inventory:List[Item], private var blueDot:Room) {
/**
 * parses and processes command provided by user
 * possible commands = get item from inventory, add item to inventory, list valid commands, move, look, and quit
 * @param: string input by user
 * @return boolean- true if valid command, false if invalid command
 */
  
def processCommand(command: String): Boolean = {
  var command = readLine()
  if (command.contains("get")) {
    var command1 = command.stripPrefix("get ")
    getFromInventory(command1) 
 
    if(command.contains("add")) {
     command1 = command.stripPrefix("add ")
     command1
     addToInventory(???) 
    
   if(command.contains("list")) {
   println(inventoryListing(inventory)) 
   
 if(command.contains("move")) { 
   command1 = command.stripPrefix("move ")
   move(command1) 
        } 
      }
    }
   if(command.contains("look")) {
     look()
   }
   if(command.contains("quit")) {
     ???
   }
    true } 
 else false 
}

/**
 * Gets item from inventory if possible and returns it
 * @return Item/true if item is in inventory, false if not
 */
def getFromInventory(itemName: String): Boolean =  { 
   val index = inventory.indexOf("itemName") 
   if (inventory.contains(itemName)) {
     inventory(index) 
     true} else false

}

 /**
 * Adds item to inventory
 * @return Unit
 */

def addToInventory(item: Item): Unit ={inventory = inventory:+ (item)}

/**
 * builds a string w/ contents of inventory for printing
 * @param List[Item]
 * @return string of inventory list
 */
def inventoryListing(I:List[Item]): String = {
 var listing = inventory.toString
 listing 
}

/**
 * 
 * @param string (north, south, east, west, up, down)
 * @return boolean- true if player can move, false if player cannot
 */

def move(dir: String): Boolean = { 
  var direction = 0
  if (dir.contains("north")) direction = 0 
  if (dir.contains("south")) direction = 1
  if (dir.contains("east")) direction = 2
  if (dir.contains("west")) direction = 3
  if (dir.contains ("up")) direction = 4
  if (dir.contains("down")) direction = 5
  val exitTest = blueDot.Exit(direction)
  if(exitTest >= 0) { 
    blueDot = rooms(exitTest)
    true} 
  else false
}
/**
 * re-prints description of room
 */
def look():Unit = {
  println(blueDot.desc)
}

/**
 * prints out options/formats for valid commands
 */
def help():Unit = {
  println("Command options")
  println("get + [item name]- gets item from your inventory")
  println("add + [item name]- adds item to your inventory")
  println("list- lists items currently in your inventory")
  println("move + [north, south, east, west, up, or down]")
  println("look- reprints room description")
}
}
/**
 * Companion object for class Player
 * 
 */
object Player {
  
}