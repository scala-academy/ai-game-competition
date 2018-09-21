package server.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import server.actors.GameActor.{Done, StartGame}
import server.actors.Protocol.{Attack, GetAttack, ProcessAttackResult}
import server.battleship.{GameState, Player, Win}

import scala.concurrent.duration._

object GameActor {

  case object StartGame

  case object Done

  def props(player1: Player, player2: Player): Props = Props(new GameActor(player1, player2))
}

class GameActor(player1: Player, player2: Player) extends Actor {
  implicit val timeout = Timeout(25 millis)

  println("Game actor created")

  val player1Actor = context.actorOf(PlayerActor.props(player1), "Player1")
  val player2Actor = context.actorOf(PlayerActor.props(player2), "Player2")
  val players: Map[Player, ActorRef] = Map(player1 -> player1Actor, player2 -> player2Actor)

  val initialGame = GameState.create(player1, player2)

  override def receive: Receive = {
    case StartGame =>
      players(initialGame.playerOnTurn) ! GetAttack
      context.become(ongoingGame(initialGame, context.sender()))
  }

  def ongoingGame(game: GameState, parent: ActorRef): Receive = {
    case Attack(row, col) =>
      val (newGame, attackResult) = game.processMove(row, col)
      players(game.playerOnTurn) ! ProcessAttackResult(attackResult)
      println(newGame.gameStateAsString)
      if (attackResult != Win) {
        context.become(ongoingGame(newGame, parent))
        players(newGame.playerOnTurn) ! GetAttack
      } else {
        parent ! Done
      }
    case otherMsg =>
      println(s"Not handling $otherMsg")
  }

}
