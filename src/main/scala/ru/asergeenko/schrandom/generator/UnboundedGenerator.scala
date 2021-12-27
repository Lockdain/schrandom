package ru.asergeenko.schrandom.generator

import ru.asergeenko.schrandom.intf.AbstractGenerator
import ru.asergeenko.schrandom.intf.connector.KafkaProducer
import ru.asergeenko.schrandom.settings.GeneratorBehavior

class UnboundedGenerator(topic: String, behavior: GeneratorBehavior) extends AbstractGenerator with Runnable {
  val eventFlooder = new EventFlooder(behavior.schema)
  val kafkaProducer = new KafkaProducer

  override def run: Unit = {
    kafkaProducer.publishJson(topic, eventFlooder.createSingleJsonEvent)
  }

  override def getBehavior(): GeneratorBehavior = behavior
}
