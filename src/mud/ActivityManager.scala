package mud
//:TODO Activity Manager
import akka.actor.Actor

class ActivityManager extends Actor {
  import ActivityManager._
  private def hp(e1:Event,e2:Event):Boolean = {
   e1.delay < e2.delay
  }
  var activitypq = new PriorityQueue(hp)
  def receive = {
    case CheckInput => activitypq.peek
    case ScheduleActivity(a) => activitypq.enqueue(a)
    case m => println("something went wrong")
  }
}

object ActivityManager {
  case class ScheduleActivity(activity:Event)
  case object CheckInput
}