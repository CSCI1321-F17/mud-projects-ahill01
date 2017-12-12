package mud
//:TODO Activity Manager
import akka.actor.Actor
import akka.actor.ActorRef

class ActivityManager extends Actor {
  import ActivityManager._

  def hp(e1: Event, e2: Event): Boolean = {
    e1.time < e2.time
  }
  private var time = 0
  private val activitypq = new PriorityQueue(hp)
  def receive = {
    case CheckActivities => {
       while (!activitypq.isEmpty && activitypq.peek.time <= time) {
        val doThis = activitypq.dequeue(); println("dQ")
        val msg = doThis.message
        doThis.whoTo ! doThis.message
        time += 1
      }

    }
    case ScheduleActivity(delay, whoTo, message) =>
      activitypq.enqueue(new Event(delay + time, whoTo, message)); println("scheduled")
    case m => println("something went wrong")
  }
}

object ActivityManager {
  case class ScheduleActivity(val delay: Int, val whoTo: ActorRef, val message: Any)
  case object CheckActivities
  private class Event(val time: Int, val whoTo: ActorRef, val message: Any) {
  }
}