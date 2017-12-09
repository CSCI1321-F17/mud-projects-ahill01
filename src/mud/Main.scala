package mud
//:TODO add NPCs w/ random movement
//: TODO add a linked list (mutable & sorted)
//:TODO add combat (equip/unequip/kill/flee command processing, players/NPCs take damage)
//:TODO priority queue (sorted linked list based PQ and Heap based PQ)
//:TODO List of players, NPCs in room
//TODO won't remove items from list (either inventory or room list)
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
 
  val pm = system.actorOf(Props[PlayerManager], "PlayerManager")
  val rm = system.actorOf(Props[RoomManager], "RoomManager")
  val npcm = system.actorOf(Props[NPCManager],"NPCManager")
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
      pm ! PlayerManager.PrintSomething(usrname + " has logged in")
      out.println("Welcome to the Library, " + usrname + "!" + "Available commands: get, add, list, move, look, quit, help, say, tell, equip, unequip, shortestPath, and kill.")
    }
  }

}