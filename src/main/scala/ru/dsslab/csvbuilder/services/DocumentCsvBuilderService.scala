package ru.dsslab.csvbuilder.services

import ru.dsslab.csvbuilder.json.ServiceFormats._
import ru.dsslab.csvbuilder.model._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Документ может содержать различные поля, среди которых могут встретиться список категорий "categories" и список комментариев "comments".
  * Можно оставить комментарий на категорию (у комментария есть дополнительное поле category).
  * Если в запросе на выгрузку среди полей есть поле "categories", то нужно на каждую категорию создать отдельный столбец.
  * Если в запросе на выгрузку установлен параметр addCategoryCommentColumn, то нужно после каждого столбца категорий добавить столбец с комментариями.
  * Остальные поля нужно выводить как есть (предобразовав json в строку).
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
object DocumentCsvBuilderService {
  def buildCategoryField(category: Category): String = s"_category.${category.id}"

  def buildCategoryCommentField(category: Category): String = s"_category_comment.${category.id}"

  def isCategoryColumn(column: String): Boolean = column.startsWith("_category.")

  def isCategoryCommentColumn(column: String): Boolean = column.startsWith("_category_comment.")

  def parseCategoryIdFromCategoryColumn(column: String): Option[String] = {
    val maybeCategoryId = column.replaceFirst("_category.", "")
    if (maybeCategoryId.nonEmpty) {
      Some(maybeCategoryId)
    } else {
      None
    }
  }
}

class DocumentCsvBuilderService(
                                 categoryService: CategoryService,
                                 searchService: SearchService,
                               )(implicit ec: ExecutionContext) {

  import DocumentCsvBuilderService._

  def build(data: BuildCsvData): Future[List[List[String]]] = {
    val fieldSet = data.fields.toSet
    for {
      documents <- searchService.getDocuments(SearchData(fieldSet))
      categories <- if (fieldSet.contains("categories")) {
        categoryService.getCategories()
      } else {
        Future.successful(List.empty[Category])
      }
    } yield {
      val columns = "_id" :: data.fields
        .flatMap({
          case fieldName if fieldName == "categories" && data.addCategoryCommentColumn =>
            categories.flatMap(category => List(buildCategoryField(category), buildCategoryCommentField(category)))
          case fieldName if fieldName == "categories" =>
            categories.map(buildCategoryField)
          case fieldName =>
            List(fieldName)
        })
      documents
        .map(document => {
          columns.map({
            case column if column == "_id" =>
              document.id
            case column if isCategoryColumn(column) =>
              (for {
                categoriesRaw <- document.fields.get("categories")
                documentCategories <- categoriesRaw.asOpt[List[Category]]
                categoryId <- parseCategoryIdFromCategoryColumn(column)
              } yield {
                val documentCategorySet = documentCategories.map(_.id).toSet
                if (documentCategorySet.contains(categoryId)) {
                  "+"
                } else {
                  "-"
                }
              })
                .getOrElse("")
            case column if isCategoryCommentColumn(column) =>
              (for {
                commentsRaw <- document.fields.get("comments")
                documentComments <- commentsRaw.asOpt[List[Comment]]
                categoryId <- parseCategoryIdFromCategoryColumn(column)
              } yield {
                documentComments
                  .collect({ case Comment(text, Some(commentCategoryId)) if commentCategoryId == categoryId => text })
                  .mkString("\n")
              })
                .getOrElse("")
            case column =>
              document.fields.get(column).map(_.toString()).getOrElse("")
          })
        })
    }
  }
}
