name := """FreelancerAnalyser"""
organization := "soen6441"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.8"

libraryDependencies += guice

libraryDependencies ++= Seq(
  javaWs
)
libraryDependencies += ws
libraryDependencies += ehcache
libraryDependencies += "org.mockito" % "mockito-core" % "4.4.0" % "test"
val AkkaVersion = "2.6.19"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-protobuf-v3" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion % Test
