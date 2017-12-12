package mud
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import RoomManager._
import Player._
import Item._
import BSTMap._
import NPC._

class Room(val keyword: String, val name: String, val desc: String, private var itemsList: List[Item], private var _exitNames: Array[String]) extends Actor {
  println("Made room: " + name)
  val exitNames = _exitNames
  val dirMap = Map[String, Int](("north", 0), ("south", 1), ("east", 2), ("west", 3), ("up", 4), ("down", 5))
  val charList = new SLL[ActorRef]()
  private var exits: Array[Option[ActorRef]] = Array.empty

  import Room._

  def receive = {
    case LinkExits(rooms) =>
      exits = exitNames.map(rooms.get)
      sender ! RoomManager.ExitInfo(keyword, exitNames)

    case GetItem(itemName) => {
      sender ! Player.TakeItem(this.getItem(itemName))
    }
    // Call your code to handle this and send something back to the sender/player
    //if have item, send message to print "got item!" -> else "that item isn't here"

    case DropItem(item) => {
      item match {
        // Call you code to handle this.
        case Some(item) => dropItem(item)
        case None => None
      }
    }
    case CheckExit(dir) => {
      sender ! Player.TakeExit(getExit(dir)) //get Some or None
    }

    case PrintDesc => {
      sender ! PrintThisDesc(description)
    }

    case AddPlayer(playerRef) => {
      println(charList + "beforeAdd:" + this.name)
      val msg = playerRef.path.name + " has arrived"
      charList.foreach(e => e.value ! Player.PrintThis(msg))
      charList.add(playerRef)
      println(charList + "afterAdd:" + this.name)
    }

    case RemovePlayer(playerRef) => {
      println(charList + "beforeRemove:" + this.name)
      val index = charList.indexOf(playerRef)
      //  println(index)
      charList.remove(index)
      println(charList + "afterRemove:" + this.name)
      charList.foreach(e => e.value ! Player.PrintThis(playerRef.path.name + " has left the room"))
    }
    /*
     case RemoveNPC(NPCRef) => {
     val index = charList.indexOf(NPCRef)
      charList.remove(index)
      charList.foreach(e => e.value ! Player.PrintThis(NPCref.path.name + " has left the room"))
     }
    case m => {
      println("Oops! Bad message to room: " + m)
  }
  */
  }
  //TODO List of characters
  /**
   * Build a String with the description of the room for printing.
   * @param Array[String]
   * @return String for printing
   */
  def description(): String = {
    name + "\n" + desc + "Items: " + itemsList.map(i => i.name).mkString(",") + "People: " + charList.toString()
  }

  /**
   * Return exit information for a direction if there is an exit in that direction.
   * @param:
   * @return Option[ActorRef]
   */

  def getExit(dir: String): Option[ActorRef] = {
    exits(dirMap(dir))
  }
  /**
   * Pull an item from the room if it is there and return it.
   * @param name of item (string)
   * @return Option[Item]
   */
  def getItem(itemName: String): Option[Item] = {
    itemsList.find(_.name == itemName) match {
      case Some(item) =>
        val ret = Some(item)
        itemsList = itemsList.filter(i => i != item)
        ret
      case None => None
    }
  }
  /**
   * Adds an item to the current room.
   * @param Item
   * @return Unit
   */
  def dropItem(item: Item): Unit = {
    itemsList = itemsList :+ item
  }
}
/**
 * This companion object stores all the rooms and has methods for
 * doing the I/O.
 */
object Room {
  case class LinkExits(rooms: BSTMap[String, ActorRef])
  case class GetItem(itemName: String)
  case class DropItem(item: Option[Item])
  case class CheckExit(dir: String)
  case object PrintDesc
  case class AddPlayer(playerRef: ActorRef)
  case class RemovePlayer(playerRef: ActorRef)
  case class RemoveNPC(NPCRef: ActorRef)
  case class AddNPC(NPCRef: ActorRef)
  /**
   * @param xml Node
   * Reads in the xml file with the map.
   * @return The Map of room name => Room
   */
  def apply(n: xml.Node): (String, () => Room) = {
    val keyword = (n \ "@keyword").text.trim
    val name = (n \ "@name").text.trim
    val desc = (n \ "@description").text.trim
    val items = (n \ "Item").map(Item.apply).toList
    val exits = (n \ "connections").text.split(",").map(_.trim)
    for (n2 <- n \ "NPC") {
      val npcname = (n2 \ "@npcname").text.trim
      val hp = (n2 \ "@hp").text.trim.toInt
      val startRoom = (n2 \ "@startRoom").text.trim
      Main.npcm ! NPCManager.NewNPC(npcname, hp, startRoom)
    }
    (keyword, () => new Room(keyword, name, desc, items, exits))

  }
  val dirs = Array("north", "south", "east", "west", "up", "down")
  val charList = Seq[ActorRef]()

}
  