package ru.asergeenko.schrandom.adt

sealed abstract class MessageType(source: String)

object MessageType {
  case object JSON extends MessageType("JSON")
  case object AVRO extends MessageType("AVRO")
}
