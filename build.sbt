import Dependencies._

name := "schrandom"

version := "0.1"

scalaVersion := "2.13.7"
coverageEnabled := true
//scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  Dependencies.Tapir.AsyncMonix,
  Dependencies.Tapir.NettyServer,
  Dependencies.Tapir.Core,
  Dependencies.Tapir.Swagger,
  Dependencies.Avro.Core,
  Dependencies.Config.PureConfig,
  Dependencies.Logging.Logback,
  Dependencies.Monix.Core,
  Dependencies.Kafka.KafkaClient,
  "org.scalatest" %% "scalatest" % "3.3.0-SNAP3" % Test
)
