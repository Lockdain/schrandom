package ru.asergeenko.schrandom.conf

case class ServiceProps(schemaRegistry: SchemaRegistry, kafka: Kafka)

case class SchemaRegistry(host: String, port: String)
case class Kafka(host: String, port: String)