package server.battleship

import org.scalatest.{Matchers, WordSpec}

class BattleShipGameSpec extends WordSpec with Matchers {

  val game = BattleShipGame(Human, Artificial)

  "initiate" should {
    "have a grid for each player" in {
      game.playerOnTurn shouldBe Human
      game.opponent shouldBe Artificial
      game.grid1.shipPlacements shouldBe Human.shipPlacements
      game.grid2.shipPlacements shouldBe Artificial.shipPlacements


    }
  }

  "The game" should {
    "pick a random player to start" in {
      val startingPlayers = for(i <- 1 to 10) yield BattleShipGame.create(Human, Artificial).playerOnTurn
      startingPlayers.toSet.size shouldBe 2
    }
    "alternate player turns after the initial move" in {

    }
    "identify each player when she or he makes a turn" in {

    }
    "notify both players if someone won" in {

    }
    "notify the players on the results of the actions of the players" in {

    }
  }

  "attack" should {
    "return the result of the move (Hit, Miss and optional a message)" in {

    }
    "keep track of the moves done for the player (track state)" in {

    }
    "return an error when the grids are not initialized yet" in {

    }
    "return an error for invalid input" in {

    }
  }

  "currentState" should {
    "return the current state of the game" in {

    }
  }

}
