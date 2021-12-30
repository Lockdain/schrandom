import sbt._

object Dependencies {

  object Versions {
    lazy val TapirBackend = "3.3.9"
    lazy val Avro         = "1.9.2"
    lazy val PureConfig   = "0.17.1"
    lazy val AlpakkaKafka = "2.1.1"
    lazy val AkkaStream   = "2.6.8"
    lazy val Monix        = "3.4.0"
    lazy val Tapir        = "0.20.0-M3"
    lazy val KafkaClient  = "2.6.0"
  }

  object Config {
    lazy val PureConfig = "com.github.pureconfig" %% "pureconfig" % Versions.PureConfig
  }

  object Logging {
    lazy val Logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
    lazy val All     = Seq(Logback)
  }

  object Tapir {
    lazy val Core        = "com.softwaremill.sttp.tapir"   %% "tapir-core"                      % Versions.Tapir
    lazy val AsyncMonix  = "com.softwaremill.sttp.client3" %% "async-http-client-backend-monix" % Versions.TapirBackend
    lazy val NettyServer = "com.softwaremill.sttp.tapir"   %% "tapir-netty-server"              % Versions.Tapir
    lazy val Swagger     = "com.softwaremill.sttp.tapir"   %% "tapir-swagger-ui-bundle"         % Versions.Tapir
  }

  object Kafka {
    lazy val KafkaClient = "org.apache.kafka" % "kafka-clients" % Versions.KafkaClient
  }

  object Avro {
    lazy val Core = "org.apache.avro" % "avro" % Versions.Avro
  }

  object Monix {
    lazy val Core = "io.monix" %% "monix" % Versions.Monix
  }

}
