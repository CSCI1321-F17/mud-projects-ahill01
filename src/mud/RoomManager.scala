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
  val rooms = new BSTMap[String,ActorRef](_.compare(_))
  mapData.foreach(rooms += _)
 
  context.children.foreach(_ ! Room.LinkExits(rooms))
  
  def findPath(roomName:String):Unit = {
    ???
  }
  
  
  import RoomManager._
  
  def receive = {
    case AddPlayerAtStart(player, startRoom) =>
       player ! Player.EnterRoom(rooms(startRoom))
    case FindPath(roomName) => {     
      sender ! Player.PrintPath(findPath(roomName))
    }
    case m =>
      println("Oops! Bad message sent to RoomManager: "+m)
     
  }
}

object RoomManager {
  case class AddPlayerAtStart(player: ActorRef, startRoom: String)
  case class FindPath(roomName:String)
}