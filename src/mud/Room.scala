package mud
import mud.Item

class Room(val name: String, val desc: String, private var _Items: List[Item], private var _Exits: Map[String, Room]) {

  val Exit = _Exits

  val ItemsList = _Items

  /**
   * Build a String with the description of the room for printing.
   * @param Array[String]
   * @return String for printing
   */
  def description(): String = {
    name + "\n" + desc
  }

  /**
   * Return exit information for a direction if there is an exit in that direction.
   * @param:
   * @return Option[Int]
   */
  def getExit(dir: Int): Option[Int] = { ??? }

  /**
   * Pull an item from the room if it is there and return it.
   * @param name of item
   * @return boolean, true if item is removed from inventory false if not
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
  /**
   * This is the array of rooms for the game map.
   */
  val rooms = readRoomsFromFile()

  /**
   * Reads in the text file with the map.
   * @return The Map of room name => Room
   */
  def readRoomsFromFile(): Map[String, Room] = {
    val source = io.Source.fromFile("Map.txt")
    val lines = source.getLines()
    val numberRooms = lines.next
    val r = Map(lines.next -> readRoom(lines))
    source.close
    r
  }

  /**
   * Reads a single room from an iterator of strings.
   * @param lines The iterator to read the data from.
   * @return A single room pulled from that iterator.
   */
  def readRoom(lines: Iterator[String]): Room = {
    val name = lines.next()
    val desc = lines.next()
    val items = List.fill(lines.next.toInt) {
      val itm = lines.next.split(";")
      new Item(itm(0), itm(1))
    }
    val exits = lines.next().split(", *").padTo(6, "")
    new Room(name, desc, items, exits)

  }

}
  