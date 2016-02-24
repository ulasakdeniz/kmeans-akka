name := """k-means-akka"""

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "com.sksamuel.scrimage" %% "scrimage-core" % "2.1.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
