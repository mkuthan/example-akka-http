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

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import spray.can.Http

import scala.concurrent.duration._

object ExampleApplication extends App {

  val DEFAULT_PORT = 8080
  val DEFAULT_INTERFACE = "0.0.0.0"

  implicit val system = ActorSystem("example-spray-system")

  val helloService = new PleasantHelloService("Hello")
  val service = system.actorOf(Props(new RestService(helloService)))

  implicit val timeout = Timeout(5.seconds)
  IO(Http) ? Http.Bind(service, interface = DEFAULT_INTERFACE, port = DEFAULT_PORT)
}
