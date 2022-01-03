package ru.asergeenko.schrandom.intf

import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.generator.{BoundedGenerator, UnboundedGenerator}
import ru.asergeenko.schrandom.settings.{GeneratorBehavior, GeneratorType, MessageType}

object MessageGeneratorBuilder {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  def build(topic: String, behavior: GeneratorBehavior): AbstractGenerator = {
    (behavior.messageType, behavior.genType) match {
      case (MessageType.JSON, GeneratorType.UNBOUNDED) => new UnboundedGenerator(topic, behavior)
      case (MessageType.JSON, GeneratorType.BOUNDED) => new BoundedGenerator(topic, behavior)
    }
  }
}
