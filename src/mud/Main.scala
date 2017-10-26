package mud

import RoomManager._
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._


object Main extends App{
    val system =ActorSystem("MUDActors")
   Console.out.println("Hello, welcome to The Library. Available commands: get, add, list, move, look, quit, help.")
   val pm = system.actorOf(Props(new PlayerManager), "PlayerManager")
   val rm = system.actorOf(Props(new RoomManager), "RoomManager")    
   rm ! AddPlayerAtStart
   var input = Console.in.toString
   while (input != "quit") {
     import system.dispatcher
     system.scheduler.schedule(0.seconds, 0.001.seconds, pm , PlayerManager.checkInput)
   }
}