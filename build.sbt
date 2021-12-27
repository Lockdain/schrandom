import Dependencies._

name := "schrandom"

version := "0.1"

scalaVersion := "2.13.7"
//scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Seq(
  Dependencies.Tapir.AsyncMonix,
  Dependencies.Avro.Core,
  Dependencies.Config.PureConfig,
  Dependencies.Logging.Logback,
  Dependencies.Monix.Core,
  Dependencies.Monix.Kafka
)

dependencyOverrides += "org.apache.kafka" % "kafka-clients" % "2.1.0"
