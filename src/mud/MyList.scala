package mud

trait MyList[A] {
  /*
   * @param index
   * @return A
   */
  def apply(i:Int): A
   /*
   * @param index
   * @return 
   */
  def update(i:Int, a: A):Unit
   /* Updates an element of a list
   * @param index, element you want to add
   * @return Unit
   */
  def insert(i: Int, a:A): Unit
  /*Removes an element from the list
   * @param index of element you want to remove
   * @return element you removed
   */
  def remove(i:Int):A
  
  def length: Int
}