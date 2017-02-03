import asynchrony.FutureExtensions.RichFuture

import scala.async.Async._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

val f1 = Future {
  Thread.sleep(2000)
  println("running the square")
  10
}

f1.onComplete _

f1.onComplete {
  case Success(x) => println(x)
  case Failure(ex) => println(ex.getMessage)
}

Thread.sleep(4000)
f1.show()
