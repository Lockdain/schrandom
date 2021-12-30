import Dependencies._

name := "schrandom"

version := "0.1"

scalaVersion := "2.13.7"
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
  Dependencies.Kafka.KafkaClient
)
