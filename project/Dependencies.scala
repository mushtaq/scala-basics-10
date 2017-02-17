import sbt._

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val `play-json` = "com.typesafe.play" %% "play-json" % "2.6.0-M1"
  val `scala-async` = "org.scala-lang.modules" %% "scala-async" % "0.9.6"
  val `akka-stream` = "com.typesafe.akka" %% "akka-stream" % "2.4.16"
  val `akka-sse` = "de.heikoseeberger" %% "akka-sse" % "2.0.0"
  val `akka-http` = "com.typesafe.akka" %% "akka-http" % "10.0.3"
  val `play-ahc-ws-standalone` = "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.0.0-M3"
  val `jmdns` = "org.jmdns" % "jmdns" % "3.5.1"
}
