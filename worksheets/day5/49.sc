import java.util.concurrent.TimeUnit

import asynchrony.Config
import asynchrony.FutureExtensions.RichFuture

import scala.concurrent.{Future, Promise}
import scala.util.{Random, Success}
import Config.ec


def square(x: Int): Future[Int] = {
  val promise: Promise[Int] = Promise()

  Config.threadpool.schedule(
    () => promise.complete(Success(x * x)),
    Random.nextInt(5000),
    TimeUnit.MILLISECONDS
  )

  promise.future
}

val xs = (1 to 100).toList

val future: Future[List[Int]] = Future.traverse(xs) { x =>
  square(x)
}



future.show()

