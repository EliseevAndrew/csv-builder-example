package ru.dsslab.csvbuilder.services

import ru.dsslab.csvbuilder.model.Category

import scala.concurrent.Future

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
class CategoryService {
  private val categories: List[Category] = List(
    Category("cat1", "value1"),
    Category("cat2", "value2"),
    Category("cat3", "value3"),
    Category("cat4", "value4"),
    Category("cat5", "value5"),
  )

  def getCategories(): Future[List[Category]] = {
    Future.successful(categories)
  }
}
