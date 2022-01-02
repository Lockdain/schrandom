package ru.asergeenko.schrandom.connector

import io.github.embeddedkafka.{ EmbeddedKafka, EmbeddedKafkaConfig }
import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import monix.execution.Scheduler.Implicits.global
import org.apache.kafka.common.serialization.{ Deserializer, StringDeserializer }
import org.scalatest.BeforeAndAfterAll

class KafkaConnectorSpec extends AnyWordSpecLike with Matchers with BeforeAndAfterAll with EmbeddedKafka {
  val kafkaPort = 6001

  override def beforeAll = {
    EmbeddedKafka.start
  }

  "Kafka producer" should {
    "produce JSON messages to Kafka" in {
      val producer: KafkaConnector                    = new KafkaSpecificProducer
      val jsonProducer                                = producer.createJsonProducer("localhost:" + kafkaPort)
      val task                                        = Task("test")
      producer.publishJson("test", task.runToFuture, jsonProducer)
      implicit val deserializer: Deserializer[String] = new StringDeserializer
      consumeFirstMessageFrom("test") shouldBe "test"
      EmbeddedKafka.stop
      EmbeddedKafka.stopZooKeeper
    }
  }

  override def afterAll = {
    EmbeddedKafka.stop
  }
}
