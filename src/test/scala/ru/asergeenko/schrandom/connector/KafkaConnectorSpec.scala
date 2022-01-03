package ru.asergeenko.schrandom.connector

import monix.eval.Task
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import monix.execution.Scheduler.Implicits.global
import io.github.embeddedkafka.schemaregistry.{ EmbeddedKafka, EmbeddedKafkaConfig }
import org.apache.avro.Schema
import org.apache.kafka.common.serialization.{ Deserializer, StringDeserializer }
import org.scalatest.BeforeAndAfterAll
import ru.asergeenko.schrandom.generator.EventFlooder

class KafkaConnectorSpec extends AnyWordSpecLike with Matchers with BeforeAndAfterAll with EmbeddedKafka {
  val kafkaPort                            = 6005
  implicit val config: EmbeddedKafkaConfig = EmbeddedKafkaConfig(kafkaPort = kafkaPort)

  override def beforeAll: Unit = {}

  "Kafka producer" should {
    "produce JSON messages to Kafka" in {
      withRunningKafka {
        val producer: KafkaConnector                    = new KafkaSpecificProducer
        val jsonProducer                                = producer.createJsonProducer("0.0.0.0:" + kafkaPort)
        val task                                        = Task("test")
        producer.publishSingleJson("test", task.runToFuture, jsonProducer)
        implicit val deserializer: Deserializer[String] = new StringDeserializer
        consumeFirstMessageFrom("test") shouldBe "test"
      }
    }
  }

  "Kafka producer" should {
    "produce multiple JSON messages to Kafka" in {
      withRunningKafka {
        val producer: KafkaConnector = new KafkaSpecificProducer
        val jsonProducer             = producer.createJsonProducer("0.0.0.0:" + kafkaPort)
        val eventualSchema           = Task(
          Schema.parse(
            "{\n\t\"type\": \"record\",\n\t\"name\": \"Trigger\",\n\t\"namespace\": \"ru.neoflex.alfabank.dms.schema.data\",\n\t\"fields\": [\n\t\t{\n\t\t\t\"name\": \"uid\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"timestamp\",\n\t\t\t\"type\": \"long\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"source\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"customerId\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"value\",\n\t\t\t\"type\": \"long\"\n\t\t}\n\t]\n}"
          )
        ).runToFuture
        val eventFlooder             = new EventFlooder(eventualSchema)
        val eventsQty                = 10
        val events                   = eventFlooder.createMultipleJsonEvents(eventsQty)

        val topic                                       = "testMultiple"
        producer.publishMultipleJson(topic, events, jsonProducer)
        implicit val deserializer: Deserializer[String] = new StringDeserializer
        val resultList                                  = consumeNumberMessagesFrom(topic, eventsQty)
        resultList.size shouldBe eventsQty
      }
    }
  }

  override def afterAll: Unit = {
    EmbeddedKafka.stop
  }
}
