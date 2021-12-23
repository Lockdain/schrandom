package ru.asergeenko.schrandom.intf.connector

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import org.apache.avro.Schema
import org.slf4j.LoggerFactory
import pureconfig.ConfigReader.Result
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import akka.http.scaladsl.unmarshalling.Unmarshal
import ru.asergeenko.schrandom.conf.ServiceProps

object ApicurioConnector extends RegistryConnector {
  private val config: Result[ServiceProps] = ConfigSource.default.load[ServiceProps]
  private implicit val system              = ActorSystem("schema-request")
  private implicit val executionContext    = system.dispatcher
  private val logger                       = LoggerFactory.getLogger(this.getClass.toString)

  override def getSchema(schemaName: String, schemaVersion: String) = {
    import akka.http.scaladsl.client.RequestBuilding.Get
    val host             = config.map(_.schemaRegistry.host).toOption.get
    val port             = config.map(_.schemaRegistry.port).toOption.get
    val artifactEndpoint = s"/api/artifacts/$schemaName/versions/$schemaVersion"

    val getSchemaRequest = Get(host + ":" + port + artifactEndpoint)
    val eventualSchema =
      Http()
        .singleRequest(getSchemaRequest)
        .map(res =>
          Unmarshal(res).to[String]
        )
        .flatten
        .map(r => Schema.parse(r))
    eventualSchema
  }
}
