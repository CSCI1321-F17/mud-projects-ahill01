package mud

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef
import java.io.PrintStream
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket

class PlayerManager extends Actor {
 import PlayerManager._
  
  def receive = { 
  case NewPlayer(name, out, in, sock, 10) =>
      val lname = name.filter(_.isLetterOrDigit)
      if(context.child(lname).isEmpty) {
      val playerref = context.actorOf(Props(new Player(name, out, in, sock, 10)), lname)
         Main.rm ! RoomManager.AddPlayer(playerref,"MainFloor")
      } else {
        out.println("Name alread taken.")
        sock.close()
      }
  case PrintSomething(a) => {context.children.foreach(_ ! Player.PrintThis(a)) }
  case PrintToRoom(msg) => {???}
  case Say(sender, something) => { context.children.foreach(_ ! Player.PrintThis(sender + " says " +something))}
  case Tell(user,sender,something) => { context.actorSelection("akka://MUDActors/user/PlayerManager/"+user) ! Player.PrintThis(sender + " tells you " + something)}
  case CheckInput => for(f <- context.children) f ! Player.CheckInput
  case m => println("Bad message sent to PlayerManager: " + m)
  }
}

object PlayerManager {
  case object AddPlayerAtStart
  case object CheckInput
  case class PrintSomething(a:String)
  case class PrintToRoom(msg: String) //TODO print things to ppl in room only
  case class Say(sender:String, something:String)
  case class Tell(user:String,sender:String, something:String)
  case class NewPlayer(name:String, out:PrintStream, in:BufferedReader, sock:Socket, hp:Int)
}