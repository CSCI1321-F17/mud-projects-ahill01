package mud

import scala.reflect.ClassTag

class BinaryHeapPQ[A : ClassTag](hp: (A, A) => Boolean) {
  private var heap = new Array[A](30)
  private var end = 1
 /*
  * removes item from priority queue 
  */
  def dequeue(): A = ???
  /*
   * adds item to priority queue
   * 
   */
  def enqueue(a: A): Unit = {
    var bubble = end
    while (bubble > 1 && hp(a, heap(bubble/2))) {
      heap(bubble) = heap(bubble/2) 
      bubble /= 2
    }
    heap(bubble) = a
    end += 1
  }
  
  def isEmpty: Boolean = end == 1
  
  def peek: A = {
    require(!isEmpty, "Can't peek empty PQ.")
    heap(1)
  }
}