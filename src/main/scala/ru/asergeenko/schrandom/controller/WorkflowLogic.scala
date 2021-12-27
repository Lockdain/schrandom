package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import ru.asergeenko.schrandom.intf.MessageGeneratorBuilder
import ru.asergeenko.schrandom.intf.connector.ApicurioConnector
import ru.asergeenko.schrandom.settings.{ GeneratorBehavior, GeneratorType, MessageType }
import monix.execution.Scheduler.{ global => scheduler }
import org.slf4j.LoggerFactory

object WorkflowLogic {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  def initiateWorkflow(topic: String, schema: String, version: String): Cancelable = {
    logger.info(s"Workflow for topic=$topic was initiated")
    val eventualSchema = ApicurioConnector.getSchema(schema, version)
    val behavior       = GeneratorBehavior(eventualSchema, GeneratorType.UNBOUNDED, MessageType.JSON)
    val jsonGenerator  = MessageGeneratorBuilder.build(topic, behavior)

    import java.util.concurrent.TimeUnit
    val cancelable: Cancelable = scheduler
      .scheduleAtFixedRate(0, 1, TimeUnit.MILLISECONDS, jsonGenerator)

    cancelable
  }
}
