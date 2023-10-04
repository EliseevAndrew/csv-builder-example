package ru.dsslab.csvbuilder.model

/**
  *
  * @author Andrew Eliseev <a.eliseev at itgrp.ru>
  */
case class Comment(
                    text: String,
                    categoryId: Option[String] = None,
                  )
