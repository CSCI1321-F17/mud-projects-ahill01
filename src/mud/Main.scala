package mud

import RoomManager._
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import java.io.PrintStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import scala.concurrent.Future


object Main extends App{ 
  val system =ActorSystem("MUDActors")
   Console.out.println("Hello, welcome to The Library. Available commands: get, add, list, move, look, quit, help.")
   val pm = system.actorOf(Props(new PlayerManager), "PlayerManager")
   val rm = system.actorOf(Props(new RoomManager), "RoomManager")    
   
  
  val ss = new ServerSocket(12345)
  while(true) {
    import system.dispatcher
    system.scheduler.schedule(0.seconds, 1.000.seconds, pm , PlayerManager.checkInput)
    val sock = ss.accept()
    val out = new PrintStream(sock.getOutputStream)
    out.println("What's your name?")
    val in = new BufferedReader(new InputStreamReader(sock.getInputStream))
    Future {
      val name = in.readLine()
      pm ! PlayerManager.NewPlayer(name, out, in, sock)
      rm ! AddPlayerAtStart
      println(name+" has arrived")
      out.println("Welcome to the Library, "+name+"!")
    }
  }
    
}