package mud
import akka.actor.Actor
import akka.actor.ActorRef

class Event(val delay:Int, val sender:ActorRef,val message:String) {
 def recieve = {
   ???
 }
}