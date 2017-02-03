package asynchrony

import java.util.concurrent.{Executors, ScheduledExecutorService}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object Config extends Config(8)
object BlockingConfig extends Config(50)

class Config(size: Int) {
  val threadpool: ScheduledExecutorService = Executors.newScheduledThreadPool(size)

  implicit val ec = ExecutionContext.fromExecutorService(threadpool)

  implicit val actorSystem = ActorSystem("TW", ConfigFactory.load())
  implicit val mat = ActorMaterializer()
}
