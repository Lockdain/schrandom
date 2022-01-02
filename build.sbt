// Embedded Kafka / Schema Registry related
resolvers ++= Seq(
  "confluent" at "https://packages.confluent.io/maven/",
  "jitpack" at "https://jitpack.io"
)
name := "schrandom"

version := "0.0.1"

scalaVersion := "2.13.7"
//coverageEnabled := true

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
  "org.scalatest"           %% "scalatest"                      % "3.3.0-SNAP3" % Test,
  "io.github.embeddedkafka" %% "embedded-kafka-schema-registry" % "6.2.1"       % Test
)

parallelExecution in Test := false // embedded kafka may not be so friendly
