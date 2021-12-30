package ru.asergeenko.schrandom.settings

case class PublisherSettings(topic: String, schema: Option[String], version: String, period: Long)
