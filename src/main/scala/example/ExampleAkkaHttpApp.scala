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
import akka.http.scaladsl.server.Directives._
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}

object ExampleAkkaHttpApp extends App with LazyLogging with KamonMetrics
  with HeartbeatRoute
  with HelloRoute {

  private val config = ExampleAkkaHttpConf()

  private implicit val actorSystem = ActorSystem("example-akka-http")
  private implicit val executor: ExecutionContext = actorSystem.dispatcher
  private implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val heartbeatService = actorSystem.actorOf(
    HeartbeatService.props()
      .withMailbox("akka.actor.bounded-mailbox"),
    "heartbeat-service")

  private val helloService = actorSystem.actorOf(
    HelloService.props("Hello")
      .withMailbox("akka.actor.bounded-mailbox")
      .withDispatcher("akka.actor.blocking-dispatcher")
      .withRouter(SmallestMailboxPool(config.helloServiceInstances)),
    "hello-service")

  private val routes = heartbeat(heartbeatService) ~ hello(helloService)

  logger.info(s"Starting server on ${config.interface}:${config.port}")
  Http().bindAndHandle(routes, config.interface, config.port)
    .onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        logger.info(s"Server is listening on ${address.getHostString}:${address.getPort}")
        registerShutdownHook(binding)
      case Failure(ex) =>
        logger.error(s"Server could not be started", ex)
        stopAll()
    }

  private def registerShutdownHook(binding: Http.ServerBinding): Unit = {
    scala.sys.addShutdownHook {
      binding.unbind().onComplete { _ =>
        stopAll()
      }
    }
  }

  private def stopAll(): Unit = {
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, config.shutdownTimeout)

    stopKamon()
  }

}


