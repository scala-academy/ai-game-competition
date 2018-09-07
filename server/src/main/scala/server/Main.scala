package server

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import server.actors.GameActor
import server.battleship.HumanInterface

import scala.concurrent.duration._

object Main extends App {

  val system = ActorSystem("Battleship-actorsystem")

  implicit val timeout = Timeout(25 millis)

  val gameActor: ActorRef = system.actorOf(GameActor.props(new HumanInterface(HumanInterface.addShips), new HumanInterface(HumanInterface.addShips)))

}
