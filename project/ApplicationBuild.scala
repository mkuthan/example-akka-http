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

import com.typesafe.sbt.SbtAspectj
import com.typesafe.sbt.SbtAspectj.AspectjKeys
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin.Revolver

object ApplicationBuild extends Build {

  object Versions {
    val akka = "2.4.0"
    val akkaStream = "1.0"
    val kamon = "0.5.1"
  }

  val projectName = "example-spray"

  val common = Seq(
    version := "1.0",
    organization := "http://mkuthan.github.io/",
    scalaVersion := "2.11.7"
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
    "com.typesafe.akka" %% "akka-actor" % Versions.akka,
    "com.typesafe.akka" %% "akka-slf4j" % Versions.akka,
    "com.typesafe.akka" %% "akka-testkit" % Versions.akka % "test",

    "com.typesafe.akka" %% "akka-http-experimental" % Versions.akkaStream,
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % Versions.akkaStream % "test",

    "org.slf4j" % "slf4j-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",

    "io.kamon" %% "kamon-core" % Versions.kamon,
    "io.kamon" %% "kamon-system-metrics" % Versions.kamon,
    "io.kamon" %% "kamon-akka" % Versions.kamon,
    "io.kamon" %% "kamon-annotation" % Versions.kamon,
    "io.kamon" %% "kamon-log-reporter" % Versions.kamon,

    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
  )

  val customExcludeDependencies = Seq(
    "org.slf4j" % "slf4j-log4j12"
  )

  lazy val main = Project(projectName, base = file("."))
    .settings(common)
    .settings(scalacOptions ++= customScalacOptions)
    .settings(libraryDependencies ++= customLibraryDependencies)
    .settings(excludeDependencies ++= customExcludeDependencies)
    .settings(Revolver.settings)
    .settings(SbtAspectj.aspectjSettings)
    .settings(javaOptions in Revolver.reStart ++= Seq("-Xms512m", "-Xmx512m", "-Dkamon.auto-start=true"))
    .settings(javaOptions in Revolver.reStart <++= AspectjKeys.weaverOptions in SbtAspectj.Aspectj)
}
