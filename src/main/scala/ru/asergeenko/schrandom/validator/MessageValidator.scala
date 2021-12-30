package ru.asergeenko.schrandom.validator

import org.apache.avro.Schema
import org.apache.avro.specific.SpecificRecordBase

trait MessageValidator {
  def validateJson(schema: Schema, message: String): Boolean
  def validateAvro(schema: Schema, message: SpecificRecordBase): Boolean
}
