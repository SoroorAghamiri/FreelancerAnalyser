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