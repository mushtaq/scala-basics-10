package asynchrony

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, RunnableGraph, Sink}
import asynchrony.FutureExtensions.RichFuture

import scala.collection.immutable.Seq
import scala.concurrent.Future

object App3 extends App {

  import akka.stream.scaladsl.Source
  import asynchrony.Config._

  val xs = (1 to 100).toList

  Source(xs)
    .map(_ + 1)
    .filter(_ > 10)
    .map(_ - 10)

  Source(xs)
    .mapAsync(30)(Helpers.square)
    .runForeach(println)
    .show()

  val source = Source(xs)
  val flow = Flow[Int].mapAsync(30)(Helpers.square)
  val sink = Sink.seq[Int]

  private val runnableGraph: RunnableGraph[NotUsed] =
    source.via(flow).to(sink)

  private val runnableGraph3: RunnableGraph[Future[Seq[Int]]] =
    source.via(flow).toMat(sink)(Keep.right)

  private val runnableGraph4: RunnableGraph[(NotUsed, Future[Seq[Int]])] =
    source.via(flow).toMat(sink)(Keep.both)

  private val run: NotUsed = runnableGraph.run()
  private val future: Future[Seq[Int]] = runnableGraph3.run()

  private val runnableGraph2: Future[Seq[Int]] =
    source.via(flow).runWith(sink)

  actorSystem.terminate()
  threadpool.shutdown()

}
