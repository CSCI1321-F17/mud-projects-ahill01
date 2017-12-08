package mud

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

class RoomManager extends Actor {
  println("Loading rooms")
  val xData = xml.XML.loadFile("mapfile.xml")
  private var mapData = {
    (xData \ "Room").map(n => {
      val (key, builder) = Room.apply(n)
      key -> context.actorOf(Props(builder()), key)
    })
  }
  val rooms = new BSTMap[String, ActorRef](_.compare(_))
  mapData.foreach(rooms += _)

  var roomExits = Map[String, Array[String]]()

  context.children.foreach(_ ! Room.LinkExits(rooms))

  def findPath(destination: String, current: String): List[String] = {
    if (destination == current) List[String]("") else {
      val paths = for ((r, dir) <- roomExits(current).zip(Room.dirs); if roomExits.contains(r)) yield {
        dir :: findPath(destination, r)
      }
      val notEmpty = paths.filter(_.nonEmpty)
      if (notEmpty.isEmpty) Nil else notEmpty.minBy(_.length)
    }
  }

  import RoomManager._

  def receive = {
    case AddPlayer(player, room) =>
      player ! Player.EnterRoom(rooms(room))
  //    Main.pm ! PlayerManager.PrintSomething(player.usrname + " has arrived")
   //   Room.charList += player
    case AddNPC(npc, room) => {
      npc ! NPC.EnterRoom(rooms(room))
   //   Room.charList += npcref
    }
    case FindPath(destination, current) => {
      sender ! Player.PrintPath(FindPath(destination, current))
    }

    case ExitInfo(keyword, exitNames) => {
      roomExits += (keyword -> exitNames)
    }
    case m =>
      println("Oops! Bad message sent to RoomManager: " + m)

  }
}

object RoomManager {
  case class AddPlayer(player: ActorRef, room: String)
  case class FindPath(destination: String, current: String)
  case class ExitInfo(keyword: String, exitNames: Array[String])
  case class AddNPC(npc: ActorRef, room: String)
}