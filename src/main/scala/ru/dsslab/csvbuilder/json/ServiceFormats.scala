package ru.dsslab.csvbuilder.json

import play.api.libs.json.{Json, OFormat}
import ru.dsslab.csvbuilder.model.{BuildCsvData, Category, Comment, Document}

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
object ServiceFormats {
  implicit val categoryFormats: OFormat[Category] = Json.format[Category]
  implicit val commentFormats: OFormat[Comment] = Json.format[Comment]
}
