package ru.asergeenko.schrandom.intf

import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.generator.{BoundedGenerator, UnboundedGenerator}
import ru.asergeenko.schrandom.settings.{GeneratorBehavior, GeneratorType, MessageType}

object MessageGeneratorBuilder {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  def build(topic: String, behavior: GeneratorBehavior): Option[AbstractGenerator] = {
    (behavior.messageType, behavior.genType) match {
      case (MessageType.JSON, GeneratorType.UNBOUNDED) => Option(new UnboundedGenerator(topic, behavior))
      case (MessageType.JSON, GeneratorType.BOUNDED) => Option(new BoundedGenerator(topic, behavior))
      case _ => {
        logger.error("Unknown message generator type.")
        Option(Some)
      }
    }
  }
}
