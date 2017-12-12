package mud
import java.io.BufferedReader
import java.io.PrintStream
import java.net.Socket

import Main.pm
import Main.rm
import Room.GetItem
import Room.PrintDesc
import akka.actor.Actor
import akka.actor.ActorRef
class Player(name: String, val out: PrintStream, val in: BufferedReader, sock: Socket, private var hp: Int) extends Actor {

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
    if (command.contains("say")) {
      val command1 = command.stripPrefix("say ")
      pm ! PlayerManager.Say(this.name, command1)
    }
    if (command.contains("tell")) {
      val command1 = command.stripPrefix("tell ")
      val split = command1.indexOf(" ")
      val name = command1.slice(0, split)
      val message = command1.stripPrefix(name)
      pm ! PlayerManager.Tell(name, this.name, message)
    }
    if (command.contains("drop")) {
      val command1 = command.stripPrefix("drop ")
      val dItem = this.dropFromInventory(command1)
      blueDot ! Room.DropItem(this.dropFromInventory(command1))
    }
    if (command.contains("get")) {
      val command1 = command.stripPrefix("get ")
      blueDot ! GetItem(command1)

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
      out.println("Bye!")
      sock.close()
    }
    if (command.contains("shortestPath")) {
      val index = command.indexOf(" ")
      rm ! RoomManager.FindPath(command.substring(index + 1), blueDot.path.name)
    }
    //:TODO TEst equip
    if (command.contains("equip")) {
      val command1 = command.stripPrefix("equip ")
      inventory.find(_.name == command1) match {
        case Some(item) =>
           item.equipped = true
           out.println(item.name+" has been equipped. "+item.name+" has "+item.damage.toString+" damage.")
        case None => out.println("Item could not be equipped")
      }
    }

    //:TODO TEst unequip
    if (command.contains("unequip")) {
      val command1 = command.stripPrefix("unequip ")
      inventory.find(_.name == command1) match {
        case Some(item) =>
          item.equipped = false
          out.println(item.name+" has been unequipped")
        case None => out.println("Item could not be unequipped")
      }
    }
    //:TODO kill
    if (command.contains("kill")) {
      val command1 = command.stripPrefix("kill ")
      val victim = context.actorSelection("akka://MUDActors/user/PlayerManager/" + command1)
      val killEm = new Event(3, self, "Kill")
      Main.am ! ActivityManager.ScheduleActivity(killEm)
    }
  }
  /**
   * Gets item from inventory if possible and returns it
   * @return Item/true if item is in inventory, false if not
   */
  def dropFromInventory(itemName: String): Option[Item] = {
    out.println("dropping item")
    inventory.find(_.name == itemName) match {
      case Some(item) =>
        val ret = Some(item)
        inventory = inventory.filter(i => i != item)
        ret
      case None => None
    }
  }

  /*
   * Adds item to inventory
   * @return Unit
   */

  def addToInventory(item: Item): Unit = {
    out.println("adding item to inventory")
    inventory = inventory :+ item
  }

  /**
   * builds a string w/ contents of inventory for printing
   * @param List[Item]
   * @return string of inventory list
   */
  def inventoryListing(I: List[Item]): String = {
    inventory.map(i => i.name).mkString(",")
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
    out.println("shortestPath + [room name]- returns shortest path from current room to the requested room")
    out.println("eqiuip + [item name]- equips item to be used as a weapon")
    out.println("unequip + [item name]- returns item to your inventory")
    out.println("kill + [user name]- initiates combat with another user")
  }

  def receive = {
    /*
     * removes player from list of players in room they just left,
     * updates room
     * adds player to list of players in new room
     */
    case TakeExit(oroom) => oroom match {
      case Some(room) =>
        blueDot ! Room.RemovePlayer(self)
        blueDot = room
        blueDot ! Room.AddPlayer(self)
      case None => out.println("That is not an exit.")
    }
    /*
     * if item is available to take, takes item otherwise error message
     */
    case TakeItem(oitem) => oitem match {
      case Some(item) =>
        addToInventory(item)
      case None => out.println("That item cannot be added to inventory")
    }
    case PrintThisDesc(description) => out.println("description: " + description)
    /*
     * 
     */
    case EnterRoom(room) => {
      blueDot = room
    }
    
    case PrintThis(something) => out.println(something)
    case PrintPath(something) => out.println("Path:" + something)
    case Kill => 
    case CheckInput => if (in.ready) processCommand(in.readLine)
  }
}

/**
 * Companion object for class Player
 */
object Player {
  case class TakeExit(oroom: Option[ActorRef]) // to user Output
  case class TakeItem(oitem: Option[Item])
  case object CheckInput
  case class PrintThisDesc(description: String)
  case class EnterRoom(startRoom: ActorRef)
  case class PrintThis(something: String)
  case class PrintPath(something: List[String])
  case object Kill
  case object Die
}