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
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"

libraryDependencies += "org.hamcrest" % "hamcrest-all" % "1.3" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.19" % Test
