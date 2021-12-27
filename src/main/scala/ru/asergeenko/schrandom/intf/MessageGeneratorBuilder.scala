package ru.asergeenko.schrandom.intf

import org.apache.avro.Schema
import ru.asergeenko.schrandom.settings.GeneratorBehavior

object MessageGeneratorBuilder {
  def buildGenerator(schema: Schema, behavior: GeneratorBehavior): AbstractGenerator = {

  }
}
