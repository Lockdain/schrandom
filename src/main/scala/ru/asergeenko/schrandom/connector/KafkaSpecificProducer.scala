package ru.asergeenko.schrandom.connector

import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._
import monix.execution.{ CancelableFuture, Scheduler }
import org.slf4j.LoggerFactory
import org.apache.kafka.clients.producer.{ KafkaProducer, ProducerRecord }
import org.apache.kafka.common.serialization.StringSerializer

import java.util.Properties

class KafkaSpecificProducer extends KafkaConnector {
  private val config: Result[ServiceProps]  = ConfigSource.default.load[ServiceProps]
  private val logger                        = LoggerFactory.getLogger(this.getClass.toString)
  private val host                          = config.map(_.kafka.host).toOption.get
  private val port                          = config.map(_.kafka.port).toOption.get
  private implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  private val bootstrapServers = (host + ":" + port)
  override def createJsonProducer(bootstrapServers: String) = {
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

  override def publishJson(
    topic: String,
    event: CancelableFuture[String],
    producer: KafkaProducer[String, String] = producer
  ): Unit = {
    logger.trace(s"New JSON will be published to the topic=$topic")
    event.onComplete { value =>
      val record = new ProducerRecord[String, String](topic, value.get)
      producer.send(record)
    }
  }

  override def publishAvro: Unit = {}
}
