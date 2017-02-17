package asynchrony

import java.util.EventListener
import javax.jmdns.{JmDNS, ServiceEvent, ServiceListener}

import akka.Done
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Source, SourceQueueWithComplete}

import scala.concurrent.{Future, Promise}

object App11 extends App {




}

object JmDns {

  val registry: JmDNS = ???

  def track(connection: String): Source[ConnectionState, Done] = {

    val (output, input) = {
      val source: Source[ConnectionState, SourceQueueWithComplete[ConnectionState]] = Source.queue[ConnectionState](10, OverflowStrategy.fail)
      coupling(source)
    }

    val listener: ServiceListener = new ServiceListener {

      override def serviceAdded(event: ServiceEvent): Unit =
        input.foreach(_.offer(ConnectionAdded(event)))

      override def serviceResolved(event: ServiceEvent): Unit =
        input.foreach(_.offer(ConnectionResolved(event)))

      override def serviceRemoved(event: ServiceEvent): Unit =
        input.foreach(_.offer(ConnectionRemoved(event)))
    }

    registry.addServiceListener(connection, listener)

    output.mapMaterializedValue(_ => Done)
  }


  trait ConnectionState
  case class ConnectionAdded(event: ServiceEvent) extends ConnectionState
  case class ConnectionRemoved(event: ServiceEvent) extends ConnectionState
  case class ConnectionResolved(event: ServiceEvent) extends ConnectionState

  def coupling[T, M](src: Source[T, M]): (Source[T, M], Future[M]) = {
    val p = Promise[M]
    val s = src.mapMaterializedValue { m =>
      p.trySuccess(m)
      m
    }
    (s, p.future)
  }
}
