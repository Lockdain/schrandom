package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.generator.UnboundedGenerator
import ru.asergeenko.schrandom.settings.{GeneratorBehavior, MessageType}

object MessageGeneratorBuilder {
  def build(topic: String, behavior: GeneratorBehavior): AbstractGenerator = {
    behavior.messageType match {
      case MessageType.JSON => new UnboundedGenerator(topic, behavior)
    }
  }
}
