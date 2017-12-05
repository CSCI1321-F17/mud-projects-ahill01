package mud
import scala.reflect.ClassTag

class PriorityQueue[A:ClassTag](hp: (A, A) => Boolean) {
  private class Node(val data: A, var prev: Node, var next: Node)
  private var default: A = _
  private val end = new Node(default, null, null)
  end.next = end
  end.prev = end
  
  def enqueue(a: A): Unit = {
    val n = new Node(a, end, end.next)
    end.next.prev = n
    end.next = n
  }
  def dequeue(): A = {
    var rover = end.next
    var hpNode = rover
    while(rover != end) {
      if(hp(rover.data, hpNode.data)) {
        hpNode = rover
      }
      rover = rover.next
    }
    val ret = hpNode.data
    hpNode.prev.next = hpNode.next
    hpNode.next.prev = hpNode.prev
    ret
  }
  def peek: A = {
    var rover = end.next
    var hpNode = rover
    while(rover != end) {
      if(hp(rover.data, hpNode.data)) {
        hpNode = rover
      }
      rover = rover.next
    }
    hpNode.data
  }
  def isEmpty: Boolean = end.next == end
}
