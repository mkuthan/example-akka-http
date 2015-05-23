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

import akka.actor.{ActorRef, ActorSystem}
import akka.http.Http
import akka.http.server.Route
import akka.stream.ActorFlowMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

object ExampleApplication extends App {

  implicit val system = ActorSystem("example-spray")
  implicit val executor = system.dispatcher
  implicit val materializer = ActorFlowMaterializer()

  val config = ConfigFactory.load()

  val helloService = system.actorOf(HelloService.props("Hello"))

  val exampleApplication = new ExampleApplication(helloService)

  Http().bindAndHandle(exampleApplication.routes, config.getString("http.interface"), config.getInt("http.port"))
}

class ExampleApplication(helloService: ActorRef)(implicit ctx: ExecutionContext)
  extends HelloRoute {

  def routes: Route = hello(helloService)

}

