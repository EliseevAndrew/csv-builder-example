package ru.dsslab.csvbuilder.model

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
case class BuildCsvData(
                         fields: List[String] = List.empty,
                         addCategoryCommentColumn: Boolean = false,
                       )
