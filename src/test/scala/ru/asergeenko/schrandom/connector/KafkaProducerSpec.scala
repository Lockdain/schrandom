package ru.asergeenko.schrandom.connector

import io.github.embeddedkafka.EmbeddedKafka
import monix.eval.Task
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import monix.execution.Scheduler.Implicits.global


class KafkaProducerSpec extends AnyWordSpecLike with Matchers with EmbeddedKafka {
  "Kafka producer" should {
    "produce JSON messages to Kafka" in {
      withRunningKafka {
        val producer: KafkaConnector = new KafkaSpecificProducer
        val jsonProducer = producer.createJsonProducer("localhost:6001")
        val task = Task("test")
        producer.publishJson("test", task.runToFuture, jsonProducer)
      }
    }
  }
}
