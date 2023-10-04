package ru.dsslab.csvbuilder.model

import play.api.libs.json.JsValue

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
case class Document(
                     id: String,
                     fields: Map[String, JsValue] = Map.empty,
                   )
