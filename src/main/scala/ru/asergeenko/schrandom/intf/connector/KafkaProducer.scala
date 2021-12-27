package ru.asergeenko.schrandom.intf.connector

import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import org.apache.kafka.common.serialization.StringSerializer
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.ServiceProps
import pureconfig.generic.auto._

class KafkaProducer extends KafkaConnector {
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]
  private val host                         = config.map(_.kafka.host).toOption.get
  private val port                         = config.map(_.kafka.port).toOption.get
  private implicit val system              = ActorSystem("kafka-producer")

  private val producerSettings = ProducerSettings(new StringSerializer, new StringSerializer)

  override def publishAvro: Unit = {}

  override def publishJson(topic: String, event: String): Unit = {}
}
