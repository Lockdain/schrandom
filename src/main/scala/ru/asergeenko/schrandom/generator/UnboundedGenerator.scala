package ru.asergeenko.schrandom.generator

import ru.asergeenko.schrandom.intf.AbstractGenerator
import ru.asergeenko.schrandom.connector.KafkaSpecificProducer
import ru.asergeenko.schrandom.adt.GeneratorBehavior
import ru.asergeenko.schrandom.tool.Logger

class UnboundedGenerator(topic: String, behavior: GeneratorBehavior) extends AbstractGenerator with Runnable with Logger {
  val eventFlooder   = new EventFlooder(behavior.schema)
  val kafkaProducer  = new KafkaSpecificProducer

  override def run(): Unit = {
    logger.trace(s"Runnable invoked for topic=$topic")
    kafkaProducer.publishSingleJson(topic, eventFlooder.createSingleJsonEvent)
  }

  override def getBehavior: GeneratorBehavior = behavior

  override def run(eventQty: Int): Unit = {

  }
}
