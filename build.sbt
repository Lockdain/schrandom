import Dependencies._

name := "schrandom"

version := "0.1"

scalaVersion := "2.13.7"
//scalacOptions += "-Ypartial-unification"

libraryDependencies ++= Dependencies.Tapir.All :+ Dependencies.Avro.Core :+ Dependencies.Config.PureConfig :+ Dependencies.Logging.Logback
