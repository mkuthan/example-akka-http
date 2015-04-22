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

import org.scalatest.{FlatSpec, Matchers}
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

class ExampleServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with ExampleService {

  def actorRefFactory = system

  "Example service" should "return a greeting for GET requests to the root path" in {
    Get() ~> exampleRoute ~> check {
      responseAs[String] should startWith("Hello")
    }
  }

  it should "leave GET requests to other paths unhandled" in {
    Get("/kermit") ~> exampleRoute ~> check {
      handled shouldBe false
    }
  }

  it should "return a MethodNotAllowed error for PUT requests to the root path" in {
    Put() ~> sealRoute(exampleRoute) ~> check {
      status should be(MethodNotAllowed)
      responseAs[String] should be("HTTP method not allowed, supported methods: GET")
    }
  }

}
