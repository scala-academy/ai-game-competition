package server.actors

import akka.actor.{Actor, Props}
import server.battleship.Player

object PlayerActor {
  def props(player: Player): Props = Props(new PlayerActor(player))
}

class PlayerActor(playerLogic: Player) extends Actor {

  println("Player actor created")

  override def receive: Receive = {
    case "Say something" => context.sender() ! "123"
    case _ => ???
  }
}
