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

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class ExampleAkkaHttpSimulation extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")
  val exampleScenario = scenario("Example Simulation").exec(Browse.browse)

  setUp(
    exampleScenario.inject(
      atOnceUsers(5),
      nothingFor(10.seconds),
      rampUsers(100) over (20.seconds))
  ).protocols(httpConf)

}

object Browse {
  val reqName = "Say hello @{n}".replaceAllLiterally("@", "$")
  val reqUri = "/hello/@{n}".replaceAllLiterally("@", "$")

  val browse = during(40.seconds, "n") {
    exec(http(reqName).get(reqUri)).pause(1.second)
  }
}
