package ru.asergeenko.schrandom.generator

import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.intf.AbstractGenerator
import ru.asergeenko.schrandom.connector.KafkaSpecificProducer
import ru.asergeenko.schrandom.settings.GeneratorBehavior

class UnboundedGenerator(topic: String, behavior: GeneratorBehavior) extends AbstractGenerator with Runnable {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)
  val eventFlooder   = new EventFlooder(behavior.schema)
  val kafkaProducer  = new KafkaSpecificProducer

  override def run: Unit = {
    logger.trace(s"Runnable invoked for topic=$topic")
    kafkaProducer.publishSingleJson(topic, eventFlooder.createSingleJsonEvent)
  }

  override def getBehavior(): GeneratorBehavior = behavior
}
