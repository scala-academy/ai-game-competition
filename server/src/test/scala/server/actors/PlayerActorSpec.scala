package server.actors

import akka.actor.ActorSystem
import akka.testkit.{TestKit, TestProbe}
import org.scalatest.WordSpecLike
import server.actors.Protocol.Attack
import server.battleship.GridImpl.ShipPlacement
import server.battleship.Player

class PlayerActorSpec extends TestKit(ActorSystem("PlayerActor")) with WordSpecLike {

  "PlayerActor" should {
    "respond with an attack when receiving GetAttack" in new PlayerActorBehavior {
      val attackLocation = (2, 'C')

      val playerLogic = new Player {
        val shipPlacements: Set[ShipPlacement] = Set.empty

        def getAttack: (Int, Char) = attackLocation
      }

      val probe = TestProbe("Testprobe")

      handleGetAttack(playerLogic, probe.ref)

      probe.expectMsg(Attack(attackLocation._1, attackLocation._2))
    }
  }

}
