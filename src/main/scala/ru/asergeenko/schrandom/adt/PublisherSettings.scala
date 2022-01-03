package ru.asergeenko.schrandom.adt

case class PublisherSettings(topic: String, schema: Option[String], version: String, period: Long)
