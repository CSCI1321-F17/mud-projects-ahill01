package mud

class Item (val _name:String, val _desc:String, val damage:Int, val speed:Int) {
  val name = _name
  val desc = _desc
}
object Item {
  /**
   * Reads in item from xml file 
   * @return item name mapped to desc => Item
   */
  def apply(n: xml.Node): Item = {
  val iname = (n \ "@name").text.trim
  val idesc = (n \ "@description").text.trim
  val dmg = (n \ "@damage").text.trim.toInt
  val speed = (n \ "@speed").text.trim.toInt
  new Item(iname, idesc, dmg, speed)
  }
}