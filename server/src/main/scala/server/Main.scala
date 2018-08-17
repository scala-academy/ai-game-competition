package server

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import server.actors.GameActor
import server.battleship.{BattleshipGame, DummyAI, HumanInterface, RandomAttackAI}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main extends App {
  //val defaultPort = 8080

  //Await.ready(Server(defaultPort).server)


  val system = ActorSystem("Battleship-actorsystem")

  implicit val timeout = Timeout(25 millis)

  val gameActor: ActorRef = system.actorOf(GameActor.props(RandomAttackAI, DummyAI))

  //  val game = BattleshipGame.createDefaultGame

  //  game.playGameTillEnd(game.initialState)

}
