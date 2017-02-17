package asynchrony

import akka.NotUsed
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.{ActorMaterializer, ThrottleMode}
import akka.stream.scaladsl.Source
import de.heikoseeberger.akkasse.ServerSentEvent
import play.api.libs.json.Json

import scala.concurrent.duration.DurationLong

object App10 extends App {

  implicit val system = ActorSystem()
  implicit val mat = ActorMaterializer()
  import mat.executionContext

  import de.heikoseeberger.akkasse.EventStreamUnmarshalling._

  val future = Http()
    .singleRequest(HttpRequest(uri = "https://stream.wikimedia.org/v2/stream/recentchange"))
    .flatMap(response => Unmarshal(response).to[Source[ServerSentEvent, NotUsed]])

  Source
    .fromFuture(future)
    .flatMapConcat(identity)
    .map(event => event.data.map(Json.parse))
    .throttle(5, 2.second, 5, ThrottleMode.Shaping)
    .runForeach(println)

}
