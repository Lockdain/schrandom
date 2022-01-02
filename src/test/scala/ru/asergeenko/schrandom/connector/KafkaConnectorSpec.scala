package ru.asergeenko.schrandom.connector

import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import monix.execution.Scheduler.Implicits.global
import io.github.embeddedkafka.schemaregistry.{EmbeddedKafka, EmbeddedKafkaConfig}
import org.apache.kafka.common.serialization.{Deserializer, StringDeserializer}
import org.scalatest.BeforeAndAfterAll

class KafkaConnectorSpec extends AnyWordSpecLike with Matchers with BeforeAndAfterAll with EmbeddedKafka {
  val kafkaPort = 6005
  implicit val config = EmbeddedKafkaConfig(kafkaPort = kafkaPort)

  override def beforeAll: Unit = {
    EmbeddedKafka.start
  }

  "Kafka producer" should {
    "produce JSON messages to Kafka" in {
      withRunningKafka {
        val producer: KafkaConnector = new KafkaSpecificProducer
        val jsonProducer = producer.createJsonProducer("0.0.0.0:" + kafkaPort)
        val task = Task("test")
        producer.publishJson("test", task.runToFuture, jsonProducer)
        implicit val deserializer: Deserializer[String] = new StringDeserializer
        consumeFirstMessageFrom("test") shouldBe "test"
      }
    }
  }

  override def afterAll: Unit = {
    EmbeddedKafka.stop
  }
}
