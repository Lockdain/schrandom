package ru.asergeenko.schrandom.intf

import ru.asergeenko.schrandom.generator.{BoundedGenerator, UnboundedGenerator}
import ru.asergeenko.schrandom.adt.{GeneratorBehavior, GeneratorType, MessageType}
import ru.asergeenko.schrandom.tool.Logger

object MessageGeneratorBuilder extends Logger {

  def build(topic: String, behavior: GeneratorBehavior): AbstractGenerator = {
    (behavior.messageType, behavior.genType) match {
      case (MessageType.JSON, GeneratorType.UNBOUNDED) => new UnboundedGenerator(topic, behavior)
      case (MessageType.JSON, GeneratorType.BOUNDED) => new BoundedGenerator(topic, behavior)
    }
  }
}
