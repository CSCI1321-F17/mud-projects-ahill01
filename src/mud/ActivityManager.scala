package mud
//:TODO Activity Manager
import akka.actor.Actor

class ActivityManager extends Actor {
  import ActivityManager._
  var activitypq = new PriorityQueue(???)
  def receive = {
    case CheckInput => activitypq.peek
    case ScheduleActivity(a) => activitypq.enqueue(a)
    case m => println("something went wrong")
  }
}

object ActivityManager {
  case class ScheduleActivity(activity:String)
  case object CheckInput
}