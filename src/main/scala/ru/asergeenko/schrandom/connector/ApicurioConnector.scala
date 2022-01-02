package ru.asergeenko.schrandom.connector

import monix.execution.CancelableFuture
import monix.execution.Scheduler.Implicits.global
import org.apache.avro.Schema
import org.slf4j.LoggerFactory
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import ru.asergeenko.schrandom.conf.{ AnyHostPort, ServiceProps }
import sttp.client3.asynchttpclient.monix.AsyncHttpClientMonixBackend
import sttp.client3.{ asString, basicRequest, Response, UriContext }

object ApicurioConnector extends RegistryConnector {
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]
  private val logger                       = LoggerFactory.getLogger(this.getClass.toString)
  private val maybeHostPort: Option[AnyHostPort] = for {
             host <- config.map(_.schemaRegistry.host).toOption
             port <- config.map(_.schemaRegistry.port).toOption
  } yield AnyHostPort(host, port)

  override def getSchema(
  schemaName: String,
  schemaVersion: String,
  hostPort: AnyHostPort = maybeHostPort.getOrElse(AnyHostPort("", ""))
  ): CancelableFuture[Schema] = {
    val artifactEndpoint = s"${hostPort.host}:${hostPort.port}/api/artifacts/$schemaName/versions/$schemaVersion"
    logger.info(s"Artifact endpoint for schema $schemaName is $artifactEndpoint")


    val getSchemaRequest = basicRequest
      .get(uri"$artifactEndpoint")
      .response(asString.getRight)

    val eventualSchema = AsyncHttpClientMonixBackend
      .resource()
      .use { backend =>
        getSchemaRequest.send(backend).map { response: Response[String] =>
          logger.info(s"Get schema request for $schemaName was successful!")
          val schema = Schema.parse(response.body)
          schema
        }
      }
      .runToFuture
    eventualSchema
  }
}
