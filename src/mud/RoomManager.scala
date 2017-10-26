package mud

import akka.actor.Actor
import akka.actor.Props
import akka.actor.ActorRef

class RoomManager extends Actor {
  val rooms = {
    println("Loading rooms")
    val xData = xml.XML.loadFile("mapfile.xml")
    (xData \ "Room").map(n => {
      val (key, builder) = Room.apply(n) 
      key -> context.actorOf(Props(builder()), key)
    }).toMap
  }
  
  context.children.foreach(_ ! Room.LinkExits(rooms))
  
  import RoomManager._
  
  def receive = {
    case AddPlayerAtStart(player, startRoom) =>
       player ! Player.EnterRoom(rooms(startRoom))
    case m =>
      println("Oops! Bad message sent to RoomManager: "+m)
  }
}

object RoomManager {
  case class AddPlayerAtStart(player: ActorRef, startRoom: String)
}