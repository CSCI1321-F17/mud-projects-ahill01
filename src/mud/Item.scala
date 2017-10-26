package mud

class Item (val _name:String, val _desc:String) {
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
  new Item(iname, idesc)
  }
}