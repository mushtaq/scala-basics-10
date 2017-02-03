package asynchrony

import java.util.concurrent.{Executors, ScheduledExecutorService}

object Config extends Config(4)

class Config(size: Int) {
  val threadpool: ScheduledExecutorService = Executors.newScheduledThreadPool(size)
}
