package ru.asergeenko.schrandom.connector

import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.{ AnyHostPort, ServiceProps }
import pureconfig.generic.auto._
import monix.execution.{ CancelableFuture, Scheduler }
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import org.apache.kafka.common.serialization.StringSerializer
import ru.asergeenko.schrandom.connector.ApicurioConnector.config

import java.util.Properties

class KafkaSpecificProducer extends KafkaConnector {
  private val config: Result[ServiceProps]       = ConfigSource.default.load[ServiceProps]
  private val logger                             = LoggerFactory.getLogger(this.getClass.toString)
  private implicit val scheduler: Scheduler      = monix.execution.Scheduler.global
  private val maybeHostPort: Option[AnyHostPort] = for {
    host <- config.map(_.kafka.host).toOption
    port <- config.map(_.kafka.port).toOption
  } yield AnyHostPort(host, port)

  private val bootstrapServers = maybeHostPort
    .map(hostPort => {
      hostPort.host + ":" + hostPort.port
    })
    .getOrElse("")

  override def createJsonProducer(bootstrapServers: String): KafkaProducer[String, String] = {
    val kafkaProducerProps: Properties = {
      val props = new Properties()
      props.put("bootstrap.servers", bootstrapServers)
      props.put("key.serializer", classOf[StringSerializer].getName)
      props.put("value.serializer", classOf[StringSerializer].getName)
      props
    }
    val jsonProducer = new KafkaProducer[String, String](kafkaProducerProps)
    jsonProducer
  }

  private val producer = createJsonProducer(bootstrapServers)

  override def publishSingleJson(
    topic: String,
    event: CancelableFuture[String],
    producer: KafkaProducer[String, String] = producer
  ): Unit = {
    logger.trace(s"New JSON will be published to the topic=$topic")
    event.onComplete { maybeMessageBody =>
      val record =
        new ProducerRecord[String, String](topic, maybeMessageBody.getOrElse("it's empty, check the producer."))
      producer.send(record)
    }
  }

  def publishMultipleJson(
    topic: String,
    events: CancelableFuture[List[String]],
    producer: KafkaProducer[String, String]
  ): Unit = {
    logger.trace(s"New JSON list will be published to the topic=$topic")
    events.foreach { eventList =>
      eventList.map(event => producer.send(new ProducerRecord[String, String](topic, event)))
    }
  }

  override def publishAvro: Unit = {}
}
