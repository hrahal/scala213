import sbt._

object Dependencies {

  object Version {
    val akka = "2.6.0-M3"
    val circe = "0.12.0-M3"
    val akkaHttp = "10.1.8"
    val typesafeConfig = "1.3.4"
    val pureConfig = "0.11.1"
    val cats = "2.0.0-M4"
    val logback = "1.2.3"
    val scalatest = "3.0.7"
    val scalaMock = "4.2.0"
    val mockito = "1.10.19"
    val scalaLogging = "3.9.2"
    val akkaCors = "0.4.1"
  }

  object Include {
    // Web
    lazy val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.akka
    lazy val akkaStreams = "com.typesafe.akka" %% "akka-stream" % Version.akka
    lazy val akkaslf4j = "com.typesafe.akka" %% "akka-slf4j" % Version.akka
    lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
    lazy val akkaHttpCors = "ch.megard" %% "akka-http-cors" % Version.akkaCors

    // JSON serialization library
    lazy val circeCore = "io.circe" %% "circe-core" % Version.circe
    lazy val circeGeneric = "io.circe" %% "circe-generic" % Version.circe
    lazy val circeParser = "io.circe" %% "circe-parser" % Version.circe

    // Configuration
    lazy val typesafeConfig = "com.typesafe" % "config" % Version.typesafeConfig
    lazy val pureConfig = "com.github.pureconfig" %% "pureconfig" % Version.pureConfig

    // Typeclasses
    lazy val catsCore = "org.typelevel" %% "cats-core" % Version.cats

    // Logger
    lazy val logbackClassic = "ch.qos.logback" % "logback-classic" % Version.logback
    lazy val scalaLogging = "com.typesafe.scala-logging" % "scala-logging_2.12" % Version.scalaLogging

    // Testing
    lazy val akkaHttpTestKit = "com.typesafe.akka" %% "akka-http-testkit" % Version.akkaHttp % "it,test"
    lazy val akkaActorTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka % "it,test"
    lazy val akkaStreamsTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka % "it,test"
    lazy val scalatest = "org.scalatest" % "scalatest_2.12" % Version.scalatest % "it,test"
    lazy val scalaMock = "org.scalamock" % "scalamock_2.12" % Version.scalaMock % "it,test"
    lazy val mockito = "org.mockito" % "mockito-all" % Version.mockito % "it,test"

  }

  lazy val infrastructure: Seq[ModuleID] = Seq(
    Include.pureConfig,
    Include.typesafeConfig
  )

  lazy val testing: Seq[ModuleID] = Seq(
    Include.akkaHttpTestKit,
    Include.akkaActorTestKit,
    Include.akkaStreamsTestKit,
    Include.mockito,
    Include.scalatest,
    Include.scalaMock
  )

  lazy val logging: Seq[ModuleID] = Seq(
    Include.logbackClassic,
    Include.scalaLogging,
    Include.akkaslf4j
  )

  lazy val akka: Seq[ModuleID] = Seq(
    Include.akkaHttp,
    Include.akkaHttpCors,
    Include.akkaActor,
    Include.akkaStreams
  )

  lazy val circe: Seq[ModuleID] = Seq(
    Include.circeGeneric,
    Include.circeParser,
    Include.circeCore
  )

  val api: Seq[ModuleID] = akka ++ circe ++ logging ++ infrastructure ++
    testing ++ Seq(Include.catsCore)
  val common: Seq[ModuleID] = circe ++ Seq(Include.catsCore, Include.pureConfig, Include.typesafeConfig)

}
