package mud

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

class PlayerManager extends Actor {
 import PlayerManager._
  
  def receive = { 
  case Message(something) => { context.children.foreach(_ ! PrintThis(something))}
  case Tell(user,something) => { context.children.foreach(_ ! PrintThis(something))}
  case checkInput: Unit => for(f <- context.children) f ! checkInput
  case m => println("Bad message sent to PlayerManager")
  }
}

object PlayerManager {
  case object printSomething
  case object AddPlayerAtStart
  case object checkInput
  case class printSomething(a:String)
  case class Message(something:String)
  case class Tell(user:String,something:String)
}