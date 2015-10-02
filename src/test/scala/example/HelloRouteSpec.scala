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

import akka.actor.Actor
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.TestActorRef
import example.HelloService.{Hello, SayHello}
import org.scalatest.{FlatSpec, Matchers}

class HelloRouteSpec extends FlatSpec with Matchers with ScalatestRouteTest with HelloRoute {

  def actorRefFactory = system

  val anyHelloService = TestActorRef(new Actor {
    override def receive = {
      case SayHello(n) => sender ! Hello("any hello message")
    }
  })

  "HelloRoute" should "return a greeting for GET request" in {
    val helloService = TestActorRef(new Actor {
      override def receive = {
        case SayHello(123) => sender ! Hello("Hello")
      }
    })

    Get("/hello/123") ~> hello(helloService) ~> check {
      status shouldBe OK
      responseAs[String] shouldBe "Hello"
    }
  }

  it should "report internal server error if underlaying service thrown an exception" in {
    val helloService = TestActorRef(new Actor {
      override def receive = {
        case SayHello(123) => throw new RuntimeException
      }
    })

    Get("/hello/123") ~> hello(helloService) ~> check {
      status shouldBe InternalServerError
    }
  }

  it should "ignore not a number for GET request" in {
    Get("/hello/not_a_number") ~> hello(anyHelloService) ~> check {
      handled shouldBe false
    }
  }

}
