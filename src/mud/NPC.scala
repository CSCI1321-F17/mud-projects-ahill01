package mud

import akka.actor.Actor
import akka.actor.ActorRef

class NPC(val name: String, private var hp:Int) extends Actor {
 def receive = {
   case m => println("Oops! Bad message to NPC: " + m)
 } 
}

object NPC {
  def apply(n: xml.Node): NPC = {
  val npcname = (n \ "@NPC").text.trim
    new NPC(npcname, 10)
  }
}