import sbt._

object Dependencies {

  object Versions {
    lazy val Tapir        = "3.3.9"
    lazy val Avro         = "1.9.2"
    lazy val PureConfig   = "0.17.1"
    lazy val AlpakkaKafka = "2.1.1"
    lazy val AkkaStream   = "2.6.8"
    lazy val Monix        = "3.4.0"
    lazy val MonixKafka   = "1.0.0-RC6"
  }

  object Config {
    lazy val PureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig
  }

  object Logging {
    lazy val Logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    lazy val All     = Seq(Logback)
  }

  object Tapir {
    lazy val AsyncMonix = "com.softwaremill.sttp.client3" %% "async-http-client-backend-monix" % Versions.Tapir
  }

  object Kafka {
    lazy val AkkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % Versions.AlpakkaKafka
    lazy val AkkaStream      = "com.typesafe.akka" %% "akka-stream"       % Versions.AkkaStream
    lazy val All             = Seq(AkkaStream, AkkaStreamKafka)
  }

  object Avro {
    lazy val Core = "org.apache.avro" % "avro" % Versions.Avro
  }

  object Monix {
    lazy val Core  = "io.monix" %% "monix"          % Versions.Monix
    lazy val Kafka = "io.monix" %% "monix-kafka-1x" % Versions.MonixKafka
  }

}
