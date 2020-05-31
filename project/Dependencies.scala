import sbt._

object Dependencies {
  private val scalaTestVersion = "3.1.1"
  private val catsVersion = "2.1.1"
  private val http4sVersion = "0.21.4"
  private val slf4jVersion = "1.7.5"


  val tests = Seq(
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
  )

  val slf4j = Seq(
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "slf4j-simple" % slf4jVersion,
    "ch.qos.logback" % "logback-classic" % "1.2.3" % Runtime
  )

  val cats = Seq("org.typelevel" %% "cats-core" % catsVersion,
    "org.typelevel" %% "cats-effect" % catsVersion,
    "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.0.9",
    "com.softwaremill.sttp.client" %% "async-http-client-backend-fs2" % "2.0.9",
    "com.softwaremill.sttp.client" %% "async-http-client-backend-cats" % "2.0.9"
  )


  val http4s = Seq(
    "org.http4s" %% "http4s-dsl" % http4sVersion % Test,
    "org.http4s" %% "http4s-blaze-client" % http4sVersion,
    "org.http4s" %% "http4s-blaze-server" % http4sVersion,
    "org.http4s" %% "http4s-core" % http4sVersion,
    "org.http4s" %% "http4s-json4s-native" % http4sVersion ,
    "org.http4s" %% "http4s-json4s-jackson" % http4sVersion ,
    "org.http4s" %% "http4s-dsl" % http4sVersion,
    "com.softwaremill.sttp.client" %% "http4s-backend" % "2.0.9",
    "org.http4s" %% "http4s-argonaut" % http4sVersion
  )

  val jackson = Seq(
    "com.fasterxml.jackson.core" % "jackson-core" % "2.10.3",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.11.0.rc1",
    "org.json" % "json" % "20190722",
    "org.json4s" %% "json4s-jackson" % "3.7.0-M2",
    "org.json4s" %% "json4s-native" % "3.6.7",
    "org.http4s" %% "http4s-argonaut" % http4sVersion
  )

  val circe = Seq(
    "org.http4s" %% "http4s-circe" % http4sVersion,
    "io.circe" %% "circe-generic" % "0.13.0",
    "io.circe" %% "circe-literal" % "0.13.0"
  )

  val postgesql = Seq(
    "org.postgresql" % "postgresql" % "42.2.12"
  )
  val liftJson = Seq(
    "net.liftweb" %% "lift-json" % "3.4.1"
  )
  val jolbox = Seq(
    "com.jolbox" % "bonecp" % "0.8.0.RELEASE"
  )

  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
}
