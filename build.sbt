version := "1.0"
organization := "scala-academy"
name := "ai-game-competition"

lazy val scalaV = "2.12.4"
lazy val akkaV = "2.4.17"
lazy val akkaHttpV = "10.0.4"
lazy val scalaTestV = "3.0.1"
lazy val scalaCheckV = "1.13.4"
lazy val scalaLoggingV = "3.9.0"
lazy val finchVersion = "0.14.0"
lazy val circeVersion = "0.7.0"


lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % scalaTestV % "it,test",
    "org.scalacheck" %% "scalacheck" % scalaCheckV % "test",
    "junit" % "junit" % "4.8.1" % "test",
    "com.github.finagle" %% "finch-core" % finchVersion,
    "com.github.finagle" %% "finch-circe" % finchVersion,
    "com.github.finagle" %% "finch-test" % finchVersion % "test",
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV,
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  ))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
