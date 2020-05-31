import Dependencies._

name := "parsing_data_and_api"

version := "0.1"

scalaVersion := "2.13.2"

scalacOptions ++= Seq(
  "-language:higherKinds",
  "-deprecation",
  "-Ywarn-value-discard")

libraryDependencies ++= Seq(cats, http4s, jackson, circe, tests, slf4j, postgesql, liftJson, jolbox).flatten