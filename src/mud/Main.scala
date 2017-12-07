package mud
//:TODO add NPCs w/ random movement
//: TODO add a linked list (mutable & sorted)
//:TODO add combat (equip/unequip/kill/flee command processing, players/NPCs take damage)
//:TODO priority queue (sorted linked list bsed PQ and Heap based PQ)
//:TODO Not making list of Items & NPCs
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.ServerSocket

import scala.concurrent.Future
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef

object Main extends App {
  val system = ActorSystem("MUDActors")
  Console.out.println("Hello, welcome to The Library. Available commands: get, add, list, move, look, quit, help, say, tell, equip, unequip, and kill.")
  val pm = system.actorOf(Props[PlayerManager], "PlayerManager")
  val rm = system.actorOf(Props[RoomManager], "RoomManager")
  import system.dispatcher
  system.scheduler.schedule(0.1.seconds, 0.100.seconds, pm, PlayerManager.CheckInput)

  val ss = new ServerSocket(4380)
  while (true) {
    val sock = ss.accept()
    val out = new PrintStream(sock.getOutputStream)
    out.println("What's your name?")
    val in = new BufferedReader(new InputStreamReader(sock.getInputStream))

    Future {
      val usrname = in.readLine()
      pm ! PlayerManager.NewPlayer(usrname, out, in, sock, 10)
   
      println(usrname + " has arrived")
      out.println("Welcome to the Library, " + usrname + "!")
    }
  }

}