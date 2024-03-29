package mud
import scala.util.Random
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import RoomManager._
import akka.actor.ActorContext
import akka.actor.AbstractActor.ActorContext
import scala.collection.mutable.ListBuffer

class NPC(val name: String, private var hp: Int, val startRoom: String) extends Actor {
  import NPC._

  private var blueDot: ActorRef = null
  def receive = {
    case AddNPCStart(room) => {
      blueDot = room
      println("move sent to am from start")
      Main.am ! ActivityManager.ScheduleActivity(3, self, NPC.DoMove)
    }

    case Player.TakeExit(oroom) => oroom match {
      case Some(oroom) =>
        blueDot ! Room.RemovePlayer(self)
        blueDot = oroom
        blueDot ! Room.AddPlayer(self)
        Main.am ! ActivityManager.ScheduleActivity(3, self, NPC.DoMove); println("sent move to am")
      case None => None
    }
    case PrintThis(m) => None
    case DoMove => {
      // val dir = ListBuffer("north","south","east","west","up","down")
      //val random = dir.shuffle
      //random(1)
      println("doing move")
      blueDot ! Room.CheckExit("up")
    }
    case m => println("Oops! Bad message to NPC: " + m)
  }
}

object NPC {
  case class AddNPCStart(room: ActorRef)
  case class PrintThis(m: String)
  case object DoMove

  def apply(n: xml.Node): NPC = {
    val npcname = (n \ "@name").text.trim
    val hp = (n \ "@hp").text.trim.toInt
    val startRoom = (n \ "@startRoom").text.trim
    new NPC(npcname, hp, startRoom)
  }
}