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

class ExampleSimulation extends Simulation {

  val httpConf = http.baseURL("http://localhost:8080")
  val example = scenario("Example Simulation").exec(Browse.browse)

  setUp(
    example.inject(rampUsers(1000) over (10.seconds))
  ).protocols(httpConf)

}

object Browse {
  val reqName = "Say hello @{n}".replaceAllLiterally("@", "$")
  val reqUri = "/hello/@{n}".replaceAllLiterally("@", "$")

  val browse = during(5.minutes, "n") {
    exec(http(reqName).get(reqUri)).pause(3, 180)
  }
}
