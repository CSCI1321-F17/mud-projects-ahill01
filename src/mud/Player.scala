package mud
import io.StdIn._
import mud.Room
import mud.Item
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import Room._
import java.io.PrintStream
import java.net.Socket
import java.io.BufferedReader
import PlayerManager._
import Main._

class Player(name:String, val out:PrintStream, val in:BufferedReader, sock:Socket) extends Actor { 

  private var inventory = List[Item]()
  private var blueDot: ActorRef = null

  import Player._
  val input = in
  var stuffToRead = false
  if (input == null) {
    stuffToRead
  } else {
    stuffToRead = true
    stuffToRead
  }
  //input stream
  if (stuffToRead == true) {
    processCommand(input.toString)
  }
  /**
   * parses and processes command provided by user
   * possible commands = get item from inventory, add item to inventory, list valid commands, move, look, and quit
   * @param string input by user
   * @return boolean- true if valid command, false if invalid command
   */

  def processCommand(command: String): Unit = {
   if (command.contains("message")) {
     val command1 = command.stripPrefix("message ")
     pm ! PlayerManager.Message(command1)
   }
   if (command.contains("tell")) {
     val command1 = command.stripPrefix("tell ")
     val split = command1.indexOf(" ")
     val name = command1.slice(0,split)
     val message = command1.stripPrefix(name)
     pm ! PlayerManager.Tell(name,message)
   }
    if (command.contains("drop")) {
      val command1 = command.stripPrefix("drop ")
      dropFromInventory(command1)
      blueDot ! _
    }
    if (command.contains("get")) {
      val command1 = command.stripPrefix("get ")
       blueDot! GetItem(command1) 
       

    }
    if (command.contains("list")) {
      out.println("items in your inventory: " + inventoryListing(inventory))
    }

    if (command.contains("north")) {
      blueDot ! Room.CheckExit("north")
     
    } else if (command.contains("south")) {
      blueDot ! Room.CheckExit("south")
      
    } else if (command.contains("east")) {
      blueDot ! Room.CheckExit("east")
      
    } else if (command.contains("west")) {
     blueDot ! Room.CheckExit("west")
     
    } else if (command.contains("up")) {
      blueDot ! Room.CheckExit("up")
      
    } else if (command.contains("down")) {
      blueDot ! Room.CheckExit("down")
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
  def dropFromInventory(itemName: String): Option[Item] = {
     inventory.find(_.name == itemName) match {
      case Some(item) => {
        val indexNil = inventory.indexOf(Nil)
        if (inventory.contains(Nil)) {inventory.updated(indexNil, item)}
        Some(item)
      }
      case None => None
    }
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
  def move(dir: String): Boolean = {
    val exitTest = RoomManager.rooms.getOrElse(dir, "none")
    if (exitTest == "none") false
    else true
  }
  /**
   * re-prints description of room
   */
  def look(): Unit = {
    blueDot ! PrintDesc
    

  }

  /**
   * prints out options/formats for valid commands
   */
  def help(): Unit = {
   out.println("Command options")
   out.println("get + [item name]- gets item from your inventory")
   out.println("add + [item name]- adds item to your inventory")
   out.println("list- lists items currently in your inventory")
   out.println("to move type north, south, east, west, up, or down")
   out.println("look- reprints room description")
  }
  
  def receive = {
   case TakeExit(oroom) => oroom match {
     case Some(room) => 
       blueDot = room //change blueDot
     case None =>  out.println("That is not an exit.")
   }
   case TakeItem(oitem) => oitem match {
     case Some(item) => 
       addToInventory(item)
     case None => out.println("That item cannot be added to inventory")
   }
   case PrintThisDesc(description) => out.println("description: " + description)
   case EnterRoom(rooms(startRoom)) => blueDot = startRoom
   case PrintThis(something) => out.println(something)
  }
}

/**
 * Companion object for class Player
 */
object Player {
  case class TakeExit(oroom: Option[ActorRef]) // to user Output
  case class TakeItem(oitem:Option[Item])
  case object CheckInput
  case class PrintThisDesc(description:String)
  case class EnterRoom(rooms(startRoom):ActorRef)
  case class PrintThis(something:String)
}