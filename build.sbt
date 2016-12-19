// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

val akkaVersion = "2.4.14"
val akkaHttpVersion = "10.0.0"
val kamonVersion = "0.6.2"
val gatlingVersion = "2.2.3"

val commonSettings = Seq(
  name := "example-akka-http",
  version := "1.0",
  organization := "http://mkuthan.github.io/",
  scalaVersion := "2.11.8"
)

val customScalacOptions = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xfuture",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused-import"
)

val customLibraryDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",

  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",

  "com.typesafe" % "config" % "1.3.1",
  "com.iheart" %% "ficus" % "1.4.0",

  "org.slf4j" % "slf4j-api" % "1.7.22",
  "ch.qos.logback" % "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",

  "io.kamon" %% "kamon-akka" % kamonVersion,
  "io.kamon" %% "kamon-annotation" % kamonVersion,
  "io.kamon" %% "kamon-autoweave" % kamonVersion,
  "io.kamon" %% "kamon-core" % kamonVersion,
  "io.kamon" %% "kamon-jmx" % kamonVersion,

  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % "test",

  "io.gatling" % "gatling-test-framework" % gatlingVersion % "it",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % gatlingVersion % "it"
)

val customExcludeDependencies = Seq(
  "org.slf4j" % "slf4j-log4j12"
)

val customJavaOptions = Seq(
  "-Xms1024m",
  "-Xmx1024m",
  "-XX:-MaxFDLimit"
)

lazy val root = (project in file("."))
    .settings(commonSettings)
    .settings(scalacOptions ++= customScalacOptions)
    .settings(libraryDependencies ++= customLibraryDependencies)
    .settings(excludeDependencies ++= customExcludeDependencies)
    .settings(fork in run := true)
    .settings(javaOptions in run ++= customJavaOptions)
    .enablePlugins(GatlingPlugin)
    .settings(javaOptions in GatlingIt ++= customJavaOptions)
