package ru.asergeenko.schrandom.controller

import monix.execution.Cancelable
import pureconfig.ConfigSource
import ru.asergeenko.schrandom.conf.{AnyHostPort, ServiceProps}
import sttp.tapir._
import sttp.tapir.server.netty.NettyFutureServer

import scala.concurrent.ExecutionContext.Implicits.global
import pureconfig.generic.auto._
import ru.asergeenko.schrandom.adt.PublisherSettings
import ru.asergeenko.schrandom.tool.Logger
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import scala.concurrent.Future

object SchrandomLogicController extends Logger {
  private val config     = ConfigSource.default.load[ServiceProps]
  private val schedulers = scala.collection.mutable.Map[String, Cancelable]()

  private val maybeHostPort: Option[AnyHostPort] = for {
    port <- config.map(_.schrandom.port).toOption
  } yield AnyHostPort("0.0.0.0", port)

  private val engagementPath: EndpointInput[Unit] = "schrandom" / "engage"
  private val engageContinuousEndpoint            = endpoint.get
    .in(engagementPath / "continuous")
    .in(query[String]("topic")).description("Destination topic name")
    .in(query[String]("schema")).description("Schema name (as per Schema Registry)")
    .in(query[String]("version")).description("Schema version (as per Schema Registry")
    .in(query[Long]("period")).description("A period between two consecutive messages")
    .out(stringBody)
    .serverLogic { case (topic, schema, version, period) =>
      schedulers(topic) = WorkflowLogicDescriptor.initiateRegistryWorkflow(
        PublisherSettings(topic, Option(schema), version, period)
      )
      Future.successful[Either[Unit, String]](
        Right(s"Engagement request for topic=$topic, schema=$schema and version=$version is received.")
      )
    }

  private val engageLimitedMsgEndpoint = endpoint.get
    .in(engagementPath / "limited")
    .in(query[String]("topic")).description("Destination topic name")
    .in(query[String]("schema")).description("Schema name (as per Schema Registry)")
    .in(query[String]("version")).description("Schema version (as per Schema Registry")
    .in(query[Int]("msgQuantity")).description("How many messages should be published")
    .out(stringBody)
    .serverLogic { case (topic, schema, version, qty) =>
      schedulers.get(topic).foreach(_.cancel)
      schedulers(topic) = WorkflowLogicDescriptor.initiateRegistryLimitedQtyWorkflow(
        PublisherSettings(topic, Option(schema), version, 0),
        qty
      )
      Future.successful[Either[Unit, String]](
        Right(s"Limited engagement request for topic=$topic, schema=$schema and version=$version is received.")
      )
    }

  private val engageWithSchemaEndpoint = endpoint.post
    .in(engagementPath / "externalSchema")
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
      schedulers(topic) = WorkflowLogicDescriptor.initiateStandaloneWorkflow(
        PublisherSettings(topic, None, "", period),
        schema
      )
      Future.successful[Either[Unit, String]](
        Right(s"Engagement request for topic=$topic is received.")
      )
    }

  private val disengagementPath: EndpointInput[Unit] = "schrandom" / "disengage"
  private val disengageEndpoint                      = endpoint.get
    .in(disengagementPath)
    .in(query[String]("topic")).description("Topic name to stop all processes")
    .out(stringBody)
    .serverLogic { topic =>
      schedulers.get(topic).foreach(_.cancel)
      Future.successful[Either[Unit, String]](
        Right(s"Disengagement request for topic $topic is received.")
      )
    }

  val endpoints = List(
    engageContinuousEndpoint,
    engageWithSchemaEndpoint,
    disengageEndpoint,
    engageLimitedMsgEndpoint
  )

  private val swaggerEndpoints: List[ServerEndpoint[Any, Future]] = SwaggerInterpreter()
    .fromServerEndpoints[Future](SchrandomLogicController.endpoints, "Schrandom", "0.0.1")

  private val schrandomPort = maybeHostPort.map(_.port.toInt).getOrElse(1)

  NettyFutureServer()
    .addEndpoints(endpoints)
    .addEndpoints(swaggerEndpoints)
    .host("0.0.0.0")
    .port(schrandomPort)
    .start()

  logger.info(s"Engagement controller started at http://localhost:$schrandomPort.")
  logger.info(s"Swagger UI started at http://localhost:$schrandomPort/docs.")
}
