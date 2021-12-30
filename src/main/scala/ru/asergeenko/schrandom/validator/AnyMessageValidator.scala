package ru.asergeenko.schrandom.validator
import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.SpecificRecordBase
import org.slf4j.LoggerFactory

import java.io.{ ByteArrayInputStream, DataInputStream }

object AnyMessageValidator extends MessageValidator {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  override def validateJson(schema: Schema, message: String): Boolean = {
    val input = new ByteArrayInputStream(message.getBytes)
    val data  = new DataInputStream(input)

    try {
      val reader  = new GenericDatumReader[Any](schema)
      val decoder = DecoderFactory.get.jsonDecoder(schema, data)
      reader.read(AnyRef, decoder)
      true
    } catch {
      case e: Exception =>
        logger.warn(s"Was unable to validate a message against schema=${schema.getDoc}", e)
        false
    }
  }

  override def validateAvro(schema: Schema, message: SpecificRecordBase): Boolean = ???
}
