package server.battleship

import org.scalatest.{Matchers, WordSpec}

class BattleshipGameTest extends WordSpec with Matchers {

// TODO write a test for win

  "Players" should {

    "Receive win notifications when they win" in {
      def player = new Player {
        var received = false
        var attacks = DummyAI.shipPlacements.flatMap(_.getPositionPoints).toSeq

        override val shipPlacements: Set[GridImpl.ShipPlacement] = DummyAI.shipPlacements

        override def getAttack: (Int, Char) = {
          val currentAttack = attacks.head
          attacks = attacks.tail
          currentAttack
        }

        override def processAttackResult(attackResult: AttackResult): Unit = received = true
      }

      val testPlayer = player
      val battleshipGame = new BattleshipGame(testPlayer, player)

      battleshipGame.playGameTillEnd(battleshipGame.initialState)

      testPlayer.received shouldBe true
    }
  }
}
