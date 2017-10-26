package mud

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import java.io.PrintStream
import java.net.Socket
import java.io.BufferedReader

class PlayerManager extends Actor {
 import PlayerManager._
  
  def receive = { 
  case Message(something) => { context.children.foreach(_ ! PrintThis(something))}
  case Tell(user,something) => { context.children.foreach(_ ! PrintThis(something))}
  case checkInput: Unit => for(f <- context.children) f ! checkInput
  case NewPlayer(name, sock, ps, br) =>
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty) {
        context.actorOf(Props(new Player(name, out: PrintStream, in: BufferedReader, sock)), lname)
      } else {
        ps.println("Name alread taken.")
        sock.close()
      }
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
  case class NewPlayer(name:String, out:PrintStream, in:BufferedReader, sock:Socket)
}