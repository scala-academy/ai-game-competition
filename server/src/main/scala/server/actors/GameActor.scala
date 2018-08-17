package server.actors

import akka.actor.{Actor, Props}
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import server.battleship.Player

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object GameActor {

  def props(player1: Player, player2: Player): Props = Props(new GameActor(player1, player2))
}

class GameActor(player1: Player, player2: Player) extends Actor {
  implicit val timeout = Timeout(25 millis)

  println("Game actor created")

  val playerActor = context.actorOf(PlayerActor.props(player1), "Player1")

  override def receive: Receive = {
    case s: String if s.contains("who") =>
      // send a response back
      val playerResponse: Future[Any] = (playerActor ? "Say something")

      playerResponse foreach { resp =>
        context.sender() ! resp
      }
    case s: String => println(s)
    case _ => ???
  }
}
