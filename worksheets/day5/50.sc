import akka.stream.scaladsl.Source
import asynchrony.Helpers
import asynchrony.Config.mat
import com.typesafe.config.ConfigFactory

val xs = (1 to 100).toList



Source(xs)
  .mapAsync(10)(Helpers.square)
  .runForeach(println)
