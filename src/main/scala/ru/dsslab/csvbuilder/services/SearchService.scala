package ru.dsslab.csvbuilder.services

import play.api.libs.json.{JsString, Json}
import ru.dsslab.csvbuilder.json.ServiceFormats._
import ru.dsslab.csvbuilder.model.{Category, Comment, Document, SearchData}

import scala.concurrent.Future

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
class SearchService {
  private val documents = List(
    Document(
      id = "doc1",
      fields = Map(
        "categories" -> Json.toJson(
          List(
            Category("cat1", "value1"),
            Category("cat2", "value2"),
            Category("cat3", "value3"),
          ),
        ),
        "comments" -> Json.toJson(
          List(
            Comment("текст к категории1", Some("cat1")),
            Comment("текст к категории3", Some("cat3")),
          ),
        ),
        "title" -> JsString("Заголовок1"),
      ),
    ),
    Document(
      id = "doc2",
      fields = Map(
        "categories" -> Json.toJson(
          List(
            Category("cat1", "value1"),
          ),
        ),
        "title" -> JsString("Заголовок2"),
      ),
    ),
    Document(
      id = "doc3",
      fields = Map(
        "categories" -> Json.toJson(
          List(
            Category("cat3", "value3"),
          ),
        ),
        "comments" -> Json.toJson(
          List(
            Comment("текст"),
            Comment("текст к категории3", Some("cat3")),
          ),
        ),
        "title" -> JsString("Заголовок3"),
      ),
    ),
  )

  def getDocuments(data: SearchData): Future[List[Document]] = {
    Future.successful(
      documents.map(document => {
        document.copy(
          fields = document.fields.filterKeys(data.fields.contains),
        )
      })
    )
  }
}
