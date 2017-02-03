import asynchrony.FutureExtensions.RichFuture

import scala.async.Async._
import scala.concurrent.Future
import asynchrony.BlockingConfig.ec

def square(x: Int): Future[Int] = Future {
  Thread.sleep(2000)
//  println("running the square")
  x * x
}

val xs = (1 to 40).toList

val futures: List[Future[Int]] = xs.map(square)

futures
  .reduce((a, b) => a.flatMap(x => b.map(y => x + y)))
  .show()

futures
  .reduce((a, b) => async(await(a) + await(b)))
  .show()

xs.foldLeft(Future.successful(List.empty[Int])) { (future, elm) =>
  async {
    await(square(elm)) :: await(future)
  }
}.map(_.reverse).show()
