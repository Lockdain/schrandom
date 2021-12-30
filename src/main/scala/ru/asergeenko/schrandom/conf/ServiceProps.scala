package ru.asergeenko.schrandom.conf

case class ServiceProps(schemaRegistry: SchemaRegistry, kafka: Kafka, schrandom: Schrandom, swagger: Swagger)

case class SchemaRegistry(host: String, port: String)
case class Kafka(host: String, port: String)
case class Schrandom(port: String)