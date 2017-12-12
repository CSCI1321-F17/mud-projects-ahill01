package mud

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

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
  context.children.foreach(_ ! Room.LinkExits(rooms)); println("Linking Exits")

  def findPath(destination: String, current: String, breadcrumb: Set[String]): List[String] = {
    if (destination == current) List(current)
    else if (breadcrumb.contains(current)) Nil else {
      val newBreadcrumb = breadcrumb + current
      val paths = for ((r, dir) <- roomExits(current).zip(Room.dirs); if roomExits.contains(r)) yield {
        current :: dir :: findPath(destination, r, newBreadcrumb)
      }
      val notEmpty = paths.filter(_.length>=3)
      if (notEmpty.isEmpty) Nil else notEmpty.minBy(_.length)
      
      }
  }

  import RoomManager._

  def receive = {
    /*
     * adds player to room
     * tells player to enter room, tells room to add player
     */
    case AddPlayer(player, room) =>
      player ! Player.EnterRoom(rooms(room))
      val roomRef = rooms(room)
      roomRef ! Room.AddPlayer(player)
     
      case AddNPCStart(npc, room) => {
      npc ! NPC.AddNPCStart(rooms(room))
      val roomRef = rooms(room)
      roomRef ! Room.AddNPC(npc)
    }
      /*
       * adds NPC to room
       * tells NPC to enter room, tells room to add NPC
       */
    case AddNPC(npc, room) => {
      val roomRef = rooms(room)
      roomRef ! Room.AddNPC(npc)
    }
    case FindPath(destination, current) => {
      sender ! Player.PrintThis("Finding path")
      sender ! Player.PrintPath(findPath(destination, current, Set()))
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
  case class AddNPCStart(npc:ActorRef,room:String)
}