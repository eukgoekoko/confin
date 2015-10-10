organization := "ma.thele"

name := "confin"

version := "0.1"

scalaVersion := "2.11.7"

mainClass := Some("ConfinServer")

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-httpx" % "6.29.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

