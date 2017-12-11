package mud

import scala.reflect.ClassTag

class BinaryHeapPQ[A : ClassTag](hp: (A, A) => Boolean) {
  private var heap = new Array[A](30)
  private var end = 1
 /*
  * removes item from priority queue 
  */
  def dequeue(): A = {
    val ret = heap(1)
    end -= 1
    val tmp = heap(end)
    var stone = 1
    var flag = true
    while(stone*2<end && flag) {
      var priorityChild = stone*2 //make assumption that left child smallest of the two
      if((stone*2+1)<end && hp(heap(stone*2+1),heap(stone*2))) priorityChild +=1
      if(hp(heap(priorityChild),tmp)) {
        heap(stone) = heap(priorityChild)
        stone = priorityChild
      } else {
        flag = false
      }
        } 
    heap(stone) = tmp
    ret
    }
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