package mud
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import RoomManager._
import Player._
import Item._
import BSTMap._
import NPC._


class Room(val keyword: String, val name: String, val desc: String, private var _Items: List[Item], private var _exitNames: Array[String]) extends Actor {
  println("Made room: " + name)
  val exitNames = _exitNames
  val dirMap = Map[String,Int](("north",0),("south",1),("east",2),("west",3),("up",4),("down",5))
  val ItemsList = _Items
  val charList = List()
  
  private var exits: Array[Option[ActorRef]] = Array.empty
  
  import Room._
  
  def receive = {
    case LinkExits(rooms) =>
      exits = exitNames.map(rooms.get)
      sender ! RoomManager.ExitInfo(keyword,exitNames)
    case GetItem(itemName) => {
      sender ! Player.TakeItem(this.getItem(itemName))
    }
    // Call your code to handle this and send something back to the sender/player
      //if have item, send message to print "got item!" -> else "that item isn't here"
    case DropItem(item)    => {
    // Call you code to handle this.
      dropItem(item)
      this.dropItem(item)
    }
    case CheckExit(dir) => {
      sender ! Player.TakeExit(getExit(dir)) //get Some or None
    }
    case PrintDesc => {
      sender ! PrintThisDesc(description)
    }
    case m => {
      println("Oops! Bad message to room: " + m)
  }
  }
//TODO List of characters
  /**
   * Build a String with the description of the room for printing.
   * @param Array[String]
   * @return String for printing
   */
  def description(): String = {
    name + "\n" + desc + "Items: " + ItemsList + "People: "
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
    ItemsList.find(_.name == itemName) match {
      case Some(item) =>
      val ret = Some(item)
        val index = ItemsList.indexOf(item)
        if (ItemsList.contains(item)) { _Items.updated(index, Nil) }
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
    _Items = _Items :+ item
  }
}
  /**
 * This companion object stores all the rooms and has methods for
 * doing the I/O.
 */
object Room {
  case class LinkExits(rooms: BSTMap[String, ActorRef])
  case class GetItem(itemName: String)
  case class DropItem(item: Item)
  case class CheckExit(dir:String)
  case object PrintDesc
 
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
  val dirs = Array("north","south","east","west","up","down")
  val charList = Seq[ActorRef]()
  
}
  