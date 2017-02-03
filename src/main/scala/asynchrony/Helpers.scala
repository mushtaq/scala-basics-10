package asynchrony

import scala.concurrent.{Future, Promise}
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.{Random, Success}
import Config.ec

object Helpers {

  def asyncSleep(duration: FiniteDuration): Future[Unit] = {
    val promise: Promise[Unit] = Promise()

    Config.threadpool.schedule(
      () => promise.complete(Success(())),
      duration.length,
      duration.unit
    )

    promise.future
  }

  def square(x: Int): Future[Int] = {
    asyncSleep(Random.nextInt(5000).millis).map { _ =>
      println(s"squaring number: $x")
      x * x
    }
  }

}
