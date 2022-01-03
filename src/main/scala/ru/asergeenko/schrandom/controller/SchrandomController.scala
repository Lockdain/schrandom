package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import org.slf4j.LoggerFactory
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.{AnyHostPort, ServiceProps}
import sttp.tapir._
import sttp.tapir.server.netty.NettyFutureServer

import scala.concurrent.ExecutionContext.Implicits.global
import pureconfig.generic.auto._
import ru.asergeenko.schrandom.settings.PublisherSettings
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import scala.concurrent.Future

object SchrandomController {
  private val config     = ConfigSource.default.load[ServiceProps]
  private val schedulers = scala.collection.mutable.Map[String, Cancelable]()
  private val logger     = LoggerFactory.getLogger(this.getClass.toString)

  private val maybeHostPort: Option[AnyHostPort] = for {
    port <- config.map(_.schrandom.port).toOption
  } yield AnyHostPort("localhost", port)

  private val engageUnboundedEndpoint = endpoint.get
    .in("schrandom" / "engage" / "unbound")
    .in(query[String]("topic"))
    .in(query[String]("schema"))
    .in(query[String]("version"))
    .in(query[Long]("period"))
    .out(stringBody)
    .serverLogic { case (topic, schema, version, period) =>
      schedulers(topic) = WorkflowLogic.initiateRegistryWorkflow(
        PublisherSettings(topic, Option(schema), version, period)
      )
      Future.successful[Either[Unit, String]](
        Right(s"Engagement request for topic=$topic, schema=$schema and version=$version is received.")
      )
    }

  private val engageBoundedEndpoint = endpoint.get
    .in("schrandom" / "engage" / "bound")
    .in(query[String]("topic"))
    .in(query[String]("schema"))
    .in(query[String]("version"))
    .in(query[Int]("qty"))
    .out(stringBody)
    .serverLogic { case (topic, schema, version, qty) =>
      schedulers.get(topic).foreach(_.cancel)
      schedulers(topic) = WorkflowLogic.initiateRegistryLimitedQtyWorkflow(
        PublisherSettings(topic, Option(schema), version, 0),
        qty
      )
      Future.successful[Either[Unit, String]](
        Right(s"Limited engagement request for topic=$topic, schema=$schema and version=$version is received."))
    }

  private val engageWithSchemaEndpoint = endpoint.post
    .in("schrandom" / "engage")
    .in(stringBody)
    .in(
      header[String]("topic")
        .description("Topic name")
    )
    .in(
      header[Long]("period")
        .description("Message period")
    )
    .out(stringBody)
    .serverLogic { case (schema, topic, period) =>
      schedulers(topic) = WorkflowLogic.initiateStandaloneWorkflow(
        PublisherSettings(topic, None, "", period),
        schema
      )
      Future.successful[Either[Unit, String]](
        Right(s"Engagement request for topic=$topic is received.")
      )
    }

  private val disengageEndpoint = endpoint.get
    .in("schrandom" / "disengage")
    .in(query[String]("topic"))
    .out(stringBody)
    .serverLogic { topic =>
      schedulers.get(topic).foreach(_.cancel)
      Future.successful[Either[Unit, String]](
        Right(s"Disengagement request for topic $topic is received.")
      )
    }

  val endpoints = List(
    engageUnboundedEndpoint,
    engageWithSchemaEndpoint,
    disengageEndpoint,
    engageBoundedEndpoint
  )

  private val swaggerEndpoints: List[ServerEndpoint[Any, Future]] = SwaggerInterpreter()
    .fromServerEndpoints[Future](SchrandomController.endpoints, "Schrandom", "0.0.1")

  private val schrandomPort = maybeHostPort.map(_.port.toInt).getOrElse(1)

  NettyFutureServer()
    .addEndpoint(engageUnboundedEndpoint)
    .addEndpoint(disengageEndpoint)
    .addEndpoint(engageWithSchemaEndpoint)
    .addEndpoint(engageBoundedEndpoint)
    .addEndpoints(swaggerEndpoints)
    .host("0.0.0.0")
    .port(schrandomPort)
    .start()

  logger.info(s"Engagement controller started at http://localhost:$schrandomPort.")
  logger.info(s"Swagger UI started at http://localhost:$schrandomPort/docs.")
}
