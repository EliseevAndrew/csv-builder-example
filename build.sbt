lazy val projectVersion = "0.0.1"

lazy val commonSettings = Seq(
  
)

lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.12.13",
    version := projectVersion,
    organization := "ru.dsslab",
    name := "csv-builder-example",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
  )
