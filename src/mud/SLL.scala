package mud

import scala.reflect.ClassTag
import scala.collection.GenTraversableOnce
import scala.collection.LinearSeqOptimized
import scala.collection.mutable
/*
 * Singly Linked List
 */
class SLL[A: ClassTag] extends MyList[A] {

  private class Node(var value: A, var next: Node)
  private var default: A = _
  private val end = new Node(default, null)
  end.next = end
  private var size = 0

  def apply(i: Int): A = {
    if (i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for (_ <- 0 until i) rover = rover.next
    rover.value
  }

  def update(i: Int, a: A): Unit = {
    if (i < 0 || i >= size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for (_ <- 0 until i) rover = rover.next
    rover.value = a
  }

  def insert(i: Int, a: A): Unit = {
    if (i < 0 || i > size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end.next
    for (_ <- 0 until i) rover = rover.next
    val n = new Node(a, rover)
    size += 1
  }

  def remove(i: Int): A = {
    if (i < 0 || i > size) throw new IndexOutOfBoundsException(s"Index $i out of $size")
    var rover = end
    for (_ <- 0 until i) rover = rover.next
    val ret = rover.value
      rover.next = rover.next.next
      size -= 1
      ret
  }

  def length: Int = size

  def add(a: A): Unit = {
    size += 1
    end.next = new Node(a, end.next)
  }

  def contains(elem: A): Boolean = {
    var rover = end.next
    for (_ <- 0 until size) rover = rover.next
    if (rover.value == elem) true else false
  }
  def indexOf(elem: A): Int = {
    var rover = end.next
    var index = 0
    while (index < size) {
      if (rover.value == elem) return index else {
        rover = rover.next
        index += 1
      }
    }
    -1
  }

  def foreach(f: Node => Unit): Unit = {
    var rover = end.next
    while (rover != end) {
      for (_ <- 0 until size) {
        f(rover)
        rover = rover.next
      }
    }
  }
  override def toString:String = {
    var rtrn = "SLL("
     var rover = end.next
    while (rover != end) {
      for (_ <- 0 until size) {
        rtrn+=rover.value+", "
        rover = rover.next
      }
    }
    rtrn + ")"
  }

 def map[B:ClassTag](f: (A) => B):SLL[B] = {
    val newList = new SLL[B] 
    var rover = end.next
    while (rover != end) {
      for (_ <- 0 until size) {
        newList.add(f(rover.value))
        rover = rover.next
      }
    }
    newList
  }

 /*   
  * def find(elem:A):Option[A] = {
 
    var rover = end.next
   for(_ <- 0 until size)rover = rover.next
    if (rover.value == elem) rover.value else None
  }
  }
  *   */
}

 
