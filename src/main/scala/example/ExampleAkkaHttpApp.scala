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

package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging
import kamon.Kamon

import scala.concurrent.ExecutionContext

object ExampleAkkaHttpApp extends App with LazyLogging with HelloRoute {
  Kamon.start()

  implicit val actorSystem = ActorSystem("example-akka-http")
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val config = ExampleAkkaHttpConf()

  val helloService = actorSystem.actorOf(HelloService.props("Hello"), "hello-service")

  Http().bindAndHandle(hello(helloService), config.interface, config.port)
}


