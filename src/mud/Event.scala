package mud
import akka.actor.Actor
import akka.actor.ActorRef

class Event(val delay:Int, val whoTo:ActorRef,val message:Any) {
}