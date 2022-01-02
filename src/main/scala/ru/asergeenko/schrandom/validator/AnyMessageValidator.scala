package ru.asergeenko.schrandom.validator
import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.SpecificRecordBase
import org.slf4j.LoggerFactory
import scala.util.Try
import java.io.{ByteArrayInputStream, DataInputStream}

object AnyMessageValidator extends MessageValidator {
  private val logger = LoggerFactory.getLogger(this.getClass.toString)

  override def validateJson(schema: Schema, message: String): Try[Boolean] = {
    logger.trace(s"Starting to validate a message against schema ${schema.getName}")
    val input = new ByteArrayInputStream(message.getBytes)
    val data  = new DataInputStream(input)

    Try {
      val reader  = new GenericDatumReader[Any](schema)
      val decoder = DecoderFactory.get.jsonDecoder(schema, data)
      reader.read(AnyRef, decoder)
      true
    }
  }

  override def validateAvro(schema: Schema, message: SpecificRecordBase): Try[Boolean] = ???
}
