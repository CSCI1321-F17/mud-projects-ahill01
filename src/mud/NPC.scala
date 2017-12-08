package mud

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import RoomManager._
import akka.actor.ActorContext
import akka.actor.AbstractActor.ActorContext

class NPC(val name: String, private var hp: Int, val startRoom:String) extends Actor {
 import NPC._
  
  
  private var blueDot: ActorRef = null
  def receive = {
    case EnterRoom(room) => blueDot = room
    case m => println("Oops! Bad message to NPC: " + m)
  }
}

object NPC {
  case class EnterRoom(room:ActorRef)
  
  def apply(n: xml.Node): NPC = {
    val npcname = (n \ "@name").text.trim
    val hp = (n \ "@hp").text.trim.toInt
    val startRoom = (n \ "@startRoom").text.trim
    new NPC(npcname, hp, startRoom)
  }
}