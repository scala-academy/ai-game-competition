package server.actors

import akka.actor.{Actor, ActorRef, Props}
import server.actors.Protocol.{Attack, GetAttack, ProcessAttackResult}
import server.battleship.Player

object PlayerActor {
  def props(player: Player): Props = Props(new PlayerActor(player))
}

class PlayerActor(val playerLogic: Player) extends Actor with PlayerActorBehavior {

  println("Player actor created")

  override def postRestart(reason: Throwable): Unit = super.postRestart(reason)

  override def receive: Receive = {
    case GetAttack =>
      handleGetAttack(playerLogic, sender())
    case ProcessAttackResult(attackResult) => playerLogic.processAttackResult(attackResult)
  }

}

trait PlayerActorBehavior {

  private[actors] def handleGetAttack(playerLogic: Player, sender: ActorRef): Unit = {
    val (row, col) = playerLogic.getAttack
    sender ! Attack(row, col)
  }
}