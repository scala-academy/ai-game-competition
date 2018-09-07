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
      val attackRow = 2
      val attackCol = 'C'
      val attack = Attack(attackRow, attackCol)
      val probe = TestProbe("Testprobe")

      val playerLogic = new Player {
        val shipPlacements: Set[ShipPlacement] = Set.empty

        def getAttack: (Int, Char) = (attackRow, attackCol)
      }

      handleGetAttack(playerLogic, probe.ref)

      probe.expectMsg(attack)
    }
  }

}
