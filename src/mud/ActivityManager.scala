package mud
//:TODO Activity Manager
import akka.actor.Actor

class ActivityManager extends Actor {
  import ActivityManager._
  private def hp(e1:Event,e2:Event):Boolean = {
   e1.delay < e2.delay
  }
  var time = 0
  var activitypq = new PriorityQueue(hp)
  def receive = {
    case CheckActivities => {
      val next = activitypq.peek
    while(! activitypq.isEmpty && next.delay <= time) {
       val doThis = activitypq.dequeue()
        doThis.whoTo ! doThis.message
       time += 1
    }
      
    }
    case ScheduleActivity(a) => activitypq.enqueue(a)
    case m => println("something went wrong")
  }
}

object ActivityManager {
  case class ScheduleActivity(activity:Event)
  case object CheckActivities
}