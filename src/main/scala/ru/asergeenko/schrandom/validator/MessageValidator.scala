package ru.asergeenko.schrandom.validator

import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase

import scala.util.Try

trait MessageValidator {
  def validateJson(schema: Schema, message: String): Try[Boolean]
  def validateAvro(schema: Schema, message: SpecificRecordBase): Try[Boolean]
}
