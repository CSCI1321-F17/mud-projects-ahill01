package mud

import scala.collection.mutable.ArrayStack
import scala.annotation.tailrec


class BSTMap[K, V](comp: (K, K) => Int) extends collection.mutable.Map[K, V] {
  import BSTMap._
  private class Node(var key: K, var data: V) {
    var left: Node = null
    var right: Node = null
  }
  
  private var root: Node = null
  
  def +=(kv: (K, V)) = {
    def add(rover: Node): Node = {
      if (rover == null) {
        new Node(kv._1, kv._2)
      } else {
        var c = comp(kv._1, rover.key)
        if (c < 0) rover.left = add(rover.left)
        else if (c > 0) rover.right = add(rover.right)
        else {
          rover.key = kv._1
          rover.data = kv._2
        }
        rover
      }
    }
    root = add(root)
    this
  }
  
  def get(key: K): Option[V] = {
    @tailrec def getter(rover: Node): Option[Node] = {
      if(rover == null) None
      else {
        var c = comp(key, rover.key)
        if (c == 0) Some(rover)
        else if (c < 0) getter(rover.left)
        else getter(rover.right)
      }
    }
    getter(root).map(_.data)
  }
  
  def -=(key: K) = {
    def findVictim(n: Node): Node = {
      if (n == null) null
      else {
        val c = comp(key, n.key)
        if (c == 0) {
          if (n.left == null) n.right
          else if (n.right == null) n.left
          else {
            val (key, data, node) = deleteMaxChild(n.left) 
            n.left = node
            n.key = key
            n.data = data
            n
          }
        } else if (c < 0) {
          n.left = findVictim(n.left)
          n
        } else {
          n.right = findVictim(n.right)
          n
        }
      }
    }
  
    def deleteMaxChild(n: Node): (K, V, Node) = {
      if (n.right == null) {
        (n.key, n.data, n.left)
      } else {
        val (key, data, node) = deleteMaxChild(n.right)
        n.right = node
        (key, data, n)
      }
    }
    
    root = findVictim(root)
    this
  }
  
  def iterator = new Iterator[(K, V)] {
    val stack = new ArrayStack[Node]
    pushAllLeft(root)
    
    def next: (K,V) = {
      val ret = stack.pop()
      pushAllLeft(ret.right)
      ret.key -> ret.data
    }
  
    def hasNext = !stack.isEmpty
  
    def pushAllLeft(n:Node) {
     if(n!=null) {
       stack.push(n)
       pushAllLeft(n.left)
     }
    }
  }

}
object BSTMap {
  def apply[K, V](data: (K, V)*)(comp: (K, K) => Int): BSTMap[K, V] = {
    val bst = new BSTMap[K, V](comp)
    val d = data.sortWith((a, b) => comp(a._1, b._1) < 0).toIndexedSeq
    def binaryAdd(start: Int, end: Int) {
      if (start < end) {
        val mid = (start + end) / 2
        bst += d(mid)
        binaryAdd(start, mid)
        binaryAdd(mid + 1, end)
       }
     }
    binaryAdd(0, data.length)
    bst
  }
}