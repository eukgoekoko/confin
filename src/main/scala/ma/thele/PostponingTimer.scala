package ma.thele

import java.util.concurrent.atomic.AtomicInteger

import com.twitter.util.{Duration, ScheduledThreadPoolTimer, Time, Timer}

/**
 * A postponing timer using ScheduledThreadPoolTimer as an underlying timer.
 * A delay for the incoming task is based on the number of currently active tasks.
 *
 * @author Evgeniy Muravev
 * @since 10.10.2015
 */
class PostponingTimer(avgTimeMillis: Long) extends Timer {
  private val underlying = new ScheduledThreadPoolTimer(name = "workers")

  private val activeTasksCount = new AtomicInteger(0)

  private def getDelayAndInc() = Duration.fromMilliseconds(avgTimeMillis * activeTasksCount.getAndIncrement())

  def schedule(when: Time)(f: => Unit) = {
    underlying.schedule(when.plus(getDelayAndInc())) {
      activeTasksCount.decrementAndGet()
      f
    }
  }

  def schedule(when: Time, period: Duration)(f: => Unit) = {
    underlying.schedule(when.plus(getDelayAndInc()), period) {
      activeTasksCount.decrementAndGet()
      f
    }
  }

  def stop() = underlying.stop()
}
