package ru.asergeenko.schrandom.validator

import org.apache.avro.Schema
import org.scalatest.flatspec.AnyFlatSpec

class MessageValidatorSpec extends AnyFlatSpec {
  "Validator" should "validate correct JSON messages as true" in {
    val validator: MessageValidator = AnyMessageValidator
    val validMessage = "{\"uid\": \"8\", \"timestamp\": 1635452593, \"source\": \"BKI\", \"customerId\": \"cus8\", \"value\": 1550}"
    val schema = Schema.parse("{\n\t\"type\": \"record\",\n\t\"name\": \"Trigger\",\n\t\"namespace\": \"ru.neoflex.alfabank.dms.schema.data\",\n\t\"fields\": [\n\t\t{\n\t\t\t\"name\": \"uid\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"timestamp\",\n\t\t\t\"type\": \"long\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"source\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"customerId\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"value\",\n\t\t\t\"type\": \"long\"\n\t\t}\n\t]\n}")
    assert(validator.validateJson(schema, validMessage) == true)
  }

  "Validator" should "validate incorrect JSON messages as false" in {
    val validator: MessageValidator = AnyMessageValidator
    val invalidMessage = "\"customerId\": \"cus8\", \"value\": 1550}"
    val schema = Schema.parse("{\n\t\"type\": \"record\",\n\t\"name\": \"Trigger\",\n\t\"namespace\": \"ru.neoflex.alfabank.dms.schema.data\",\n\t\"fields\": [\n\t\t{\n\t\t\t\"name\": \"uid\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"timestamp\",\n\t\t\t\"type\": \"long\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"source\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"customerId\",\n\t\t\t\"type\": \"string\"\n\t\t},\n\t\t{\n\t\t\t\"name\": \"value\",\n\t\t\t\"type\": \"long\"\n\t\t}\n\t]\n}")
    assert(validator.validateJson(schema, invalidMessage) == false)
  }
}
