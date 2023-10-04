package ru.dsslab.csvbuilder

import ru.dsslab.csvbuilder.model.BuildCsvData
import ru.dsslab.csvbuilder.services.{CategoryService, DocumentCsvBuilderService, SearchService}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
object Boot extends App {
  val csvBuilderService = new DocumentCsvBuilderService(
    categoryService = new CategoryService,
    searchService = new SearchService,
  )
  val data = BuildCsvData(
    fields = List("title", "categories"),
    addCategoryCommentColumn = true,
  )
  val futureResult = csvBuilderService.build(data)
  val result = Await.result(futureResult, Duration.Inf)
  println(result)
}
