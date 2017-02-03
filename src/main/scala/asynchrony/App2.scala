package asynchrony

import scala.concurrent.Promise
import scala.util.Try

object App2 extends App {

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
    case Success(x) => println(s"done computing $x")
    case Failure(ex) => println(ex.getMessage)
  }

  def map(f: Future[Int], op: Int => Int): Future[Int] = {
    val promise: Promise[Int] = Promise()

    f.onComplete { (x: Try[Int]) =>
      val result: Try[Int] = x.map(op)
      promise.complete(result)
    }

    promise.future
  }

  println(map(f1, x => x * x).show())
  Thread.sleep(4000)
}
