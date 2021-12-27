import sbt._

object Dependencies {

  object Versions {
    lazy val Tapir        = "0.20.0-M2"
    lazy val Avro         = "1.9.2"
    lazy val PureConfig   = "0.17.1"
    lazy val AlpakkaKafka = "2.1.1"
    lazy val AkkaStream   = "2.6.8"
  }

  object Config {
    lazy val PureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig
  }

  object Logging {
    lazy val Logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    lazy val All     = Seq(Logback)
  }

  object Tapir {
    lazy val AkkaHttp = "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server" % Versions.Tapir
    lazy val Core     = "com.softwaremill.sttp.tapir" %% "tapir-core"             % Versions.Tapir
    lazy val All      = Seq(AkkaHttp, Core)
  }

  object Kafka {
    lazy val AkkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % Versions.AlpakkaKafka
    lazy val AkkaStream      = "com.typesafe.akka" %% "akka-stream"       % Versions.AkkaStream
    lazy val All             = Seq(AkkaStream, AkkaStreamKafka)
  }

  object Avro {
    lazy val Core = "org.apache.avro" % "avro" % Versions.Avro
  }

}
