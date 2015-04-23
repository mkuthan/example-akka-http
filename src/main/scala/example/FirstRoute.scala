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

import akka.actor.ActorRef
import akka.pattern.ask
import spray.routing.{HttpService, Route}

import scala.concurrent.ExecutionContext

trait FirstRoute extends HttpService {

  import Timeouts._

  def firstRoute(helloService: ActorRef)(implicit ctx: ExecutionContext): Route =
    path("first" / IntNumber) { number =>
      get {
        complete {
          (helloService ? SayHello(number)).map { case hello: String => hello }
        }
      }
    }
}
