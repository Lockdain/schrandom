package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import ru.asergeenko.schrandom.intf.MessageGeneratorBuilder
import ru.asergeenko.schrandom.intf.connector.ApicurioConnector
import ru.asergeenko.schrandom.settings.{ GeneratorBehavior, GeneratorType, MessageType }
import monix.execution.Scheduler.{global => scheduler}

object WorkflowLogic {

  def initiateWorkflow(topic: String, schema: String, version: String):Cancelable = {
    val eventualSchema = ApicurioConnector.getSchema(schema, version)
    val behavior       = GeneratorBehavior(eventualSchema, GeneratorType.UNBOUNDED, MessageType.JSON)
    val jsonGenerator = MessageGeneratorBuilder.build(topic, behavior)

    import java.util.concurrent.TimeUnit
    val cancelable: Cancelable = scheduler
      .scheduleAtFixedRate(0, 1, TimeUnit.MILLISECONDS, jsonGenerator)

    cancelable
  }
}
