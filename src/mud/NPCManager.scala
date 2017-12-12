package mud

import akka.actor.Actor
import akka.actor.Props

class NPCManager extends Actor {
 
  
  import NPCManager._
  def receive = {
    case NewNPC(npcname, hp, startRoom) => {
      val npcref = context.actorOf(Props(new NPC(npcname, hp, startRoom)), npcname)
      Main.rm ! RoomManager.AddNPCStart(npcref, startRoom)
         }
  }
}
object NPCManager {
  case class NewNPC(name: String, hp: Int, startRoom: String)
}