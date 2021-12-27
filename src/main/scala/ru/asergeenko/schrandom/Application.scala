package ru.asergeenko.schrandom

import monix.execution.Cancelable
import org.slf4j.LoggerFactory
import ru.asergeenko.schrandom.intf.connector.ApicurioConnector
import monix.execution.Scheduler.{global => scheduler}
import ru.asergeenko.schrandom.controller.SchrandomController
import ru.asergeenko.schrandom.intf.{AbstractGenerator, MessageGeneratorBuilder}
import ru.asergeenko.schrandom.settings.{GeneratorBehavior, GeneratorType, MessageCreationPolicy, MessageType}

object Application extends App {
  private val logger                           = LoggerFactory.getLogger(this.getClass.toString)
  private val eventualSchema                   = ApicurioConnector.getSchema("trigger-value", "1")
  private val behavior                         = GeneratorBehavior(eventualSchema, GeneratorType.UNBOUNDED, MessageType.JSON)
  private val jsonGenerator: AbstractGenerator = MessageGeneratorBuilder.build("test", behavior)

  import java.util.concurrent.TimeUnit

  private val cancelable: Cancelable = scheduler
    .scheduleAtFixedRate(0, 1, TimeUnit.MILLISECONDS, jsonGenerator)

  private val controller: SchrandomController.type = SchrandomController
}
