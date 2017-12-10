package mud

import scala.reflect.ClassTag

class SLL[A:ClassTag] extends MyList[A] {
 /*
  *  private class Node(var data:A, var next:Node)

  private var hd:Node = null
  private var t1:Node = null
  private var listlength = 0
  
  def +=(elem:A):SLL.this.type = {
    if (t1 == hd) {
      hd = new Node (elem, null)
      t1 = hd
    } else {
      t1.next = new Node(elem, null)
      t1 = t1.next
    }
    listlength += 1
    this
  }
  def apply
  */
 private class Node(var value: A, var next: Node)
  private var default: A = _
  private val end = new Node(default, null)
  end.next = end
  private var size = 0
  
  def apply(i: Int): A = {
    if(i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    rover.value
  }
  
  def update(i: Int, a: A): Unit = {
    if(i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    rover.value = a
  }
  
  def insert(i: Int, a: A): Unit = {
    if(i < 0 || i > size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    val n = new Node(a, rover)
    size += 1
  }
  
  def remove(i: Int): A = {
    if(i < 0 || i > size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for(_ <- 0 until i) rover = rover.next
    size -= 1
    rover.value
  }
  
  def length: Int = size
  
  def add(a: A): Unit = {
    size += 1
    val n = new Node(a, end)
  }
  
  def map[B:ClassTag](f: A => B): SLL[B] = {
    val ret = new SLL[B]()
    var rover = end.next
    while(rover!=end) {
      ret.add(f(rover.value))
    }
    ret
  }
  def contains(elem:A):Boolean = {
   var rover = end.next
    for(_ <- 0 until size)rover = rover.next
    if (rover.value == elem) true else false
  }
  def indexOf(elem:A):Int = {
    var rover = end.next
    var index = 0
    for(_ <- 0 until size)rover = rover.next
    index += 1
    if (rover.value == elem) index else indexOf(elem)
  }
  def find(key:String):Option[A] = {
    var rover = end.next
    ???  
  }
  }

 
