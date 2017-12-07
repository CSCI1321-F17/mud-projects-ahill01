package mud
import mud.Item
import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import RoomManager._
import Player._
import Item._
import BSTMap._
import NPC._


class Room(val keyword: String, val name: String, val desc: String, private var _Items: List[Item], private var NPCs:Seq[NPC], private var _exitNames: Array[String]) extends Actor {
  println("Made room: " + name)
  val exitNames = _exitNames
  val dirMap = Map[String,String](("north",exitNames(0)),("south",exitNames(1)),("east",exitNames(2)),("west",exitNames(3)),("up",exitNames(4)),("down",exitNames(5)))
  val ItemsList = _Items
  val NPCsList = NPCs
  
  private var exits: Array[Option[ActorRef]] = Array.empty
  
  import Room._
  
  def receive = {
    case LinkExits(rooms) =>
      exits = exitNames.map(rooms.get)
      sender ! RoomManager.ExitInfo(keyword,_exitNames)
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

  /**
   * Build a String with the description of the room for printing.
   * @param Array[String]
   * @return String for printing
   */
  def description(): String = {
    name + "\n" + desc + "Items: " + ItemsList.toString + "People: " + NPCsList
  }

  /**
   * Return exit information for a direction if there is an exit in that direction.
   * @param:
   * @return Option[Room]
   */
  //:TODO Get Exit
 def getExit(dir: String): Option[ActorRef] = {
     ???
 }
  /**
   * Pull an item from the room if it is there and return it.
   * @param name of item (string)
   * @return Option[Item]
   */
  def getItem(itemName: String): Option[Item] = {
    ItemsList.find(_.name == itemName) match {
      case Some(item) =>
        val indexNil = ItemsList.indexOf(Nil)
        if (ItemsList.contains(Nil)) { _Items.updated(indexNil, item) }
        Some(item)
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
 
  //:TODO add in NPCS 
  /**
   * @param xml Node
   * Reads in the xml file with the map.
   * @return The Map of room name => Room
   */
  def apply(n: xml.Node): (String, () => Room) = {
    val keyword = (n \ "@keyword").text.trim
    val name = (n \ "@name").text.trim
    val desc = (n \ "@description").text.trim
    val items = (n \ "item").map(Item.apply).toList
    val npcs = (n \ "@NPC").map(NPC.apply)
    val exits = (n \ "connections").text.split(",").map(_.trim)
     println(name + items + npcs)
    (keyword, () => new Room(keyword, name, desc, items, npcs, exits))
    
  }
  val dirs = Array("north","south","east","west","up","down")

}
  