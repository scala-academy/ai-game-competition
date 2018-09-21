package server

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import com.twitter.finagle.Http
import com.twitter.util._
import com.typesafe.scalalogging.LazyLogging
import io.finch.{Endpoint, Text, _}
import server.actors.GameActor
import server.actors.GameActor.StartGame
import server.battleship.RandomAttackAI
import util.TwitterConverters._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Server {

  def apply(port: Int): Server = new Server(port)
}

class Server private[server](port: Int) extends LazyLogging {

  val system = ActorSystem("Battleship-actorsystem")

  implicit val timeout = Timeout(25 millis)

  val api: Endpoint[String] = get("hello") { Ok("Hello, world!") }

  val start: Endpoint[String] = post("start") {
    val gameActor: ActorRef = system.actorOf(GameActor.props(RandomAttackAI, RandomAttackAI))
    scalaToTwitterFuture(gameActor ? StartGame map(_ => Ok("started")))
  }

  val endpoints = api :+: start

  val server = Http.server.serve(s":$port", endpoints.toServiceAs[Text.Plain])
  logger.debug(s"Starting server at $port")

  def terminate: Future[Unit] = {
    server.close()
  }
}