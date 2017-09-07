package MUD

class Room (val name:String, val desc: String, private var _Items: List[Item], private var Exits: Array[Int]) {
 
    val Exit = Exits
   
   val ItemsList = _Items
  
   /**
   * Build a String with the description of the room for printing.
   * @param Array[String]
   * @return String for printing
   */
  def description(d:Array[String]): String = {
     ??? 
    } 

  
  /**
  * Return exit information for a direction if there is an exit in that direction.
  * @param: 
  * @return Option[Int]
  */
    def getExit(dir:Int): Option[Int] = {???}
  
    /**
     * Pull an item from the room if it is there and return it.
     * @param item name
     * @return boolean, true if item is removed from inventory false if not
     */
  def getItem(itemName:String): Boolean = {
    val index = ItemsList.indexOf("itemName") 
   if (ItemsList.contains(itemName)) {
     _Items.updated(index,Nil)
     true} else {
       println("This item is not in your inventory")
       false} 
    }

/**
 * Adds an item to the current room.
 * @param Item
 * @return Unit
 */
  def dropItem(item:Item): Unit = {
    val indexNil = ItemsList.indexOf(Nil)
    if (ItemsList.contains(Nil)) {
      _Items.updated(indexNil,item) 
      } else (_Items :+ item)
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
   * @return The array of rooms from the file.
   */
  def readRoomsFromFile():Array[Room] = {
    val source = io.Source.fromFile("Map.txt")
    val lines = source.getLines()
    val r = Array.fill(lines.next.toInt)(readRoom(lines))
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
    val exits = lines.next().split(" ").map(_.toInt) 
    new Room (name, desc, items, exits)
  }
  
}
  