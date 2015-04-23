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
import spray.can.Http

object ExampleApplication extends App {

  import Timeouts._
  import system.dispatcher

  val DEFAULT_PORT = 8080
  val DEFAULT_INTERFACE = "0.0.0.0"

  implicit val system = ActorSystem("example-spray-system")

  val helloService = system.actorOf(Props(new HelloService("Hello")))

  val mainService = system.actorOf(Props(new RestService(helloService)))

  IO(Http) ? Http.Bind(mainService, interface = DEFAULT_INTERFACE, port = DEFAULT_PORT)
}
