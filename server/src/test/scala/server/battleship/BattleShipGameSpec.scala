package server.battleship

import org.scalatest.{Matchers, WordSpec}

class BattleShipGameSpec extends WordSpec with Matchers {

  "initiate" should {
    "create two players" in {

    }
    "have a grid for each player" in {

    }
    "should let the players place their ships" in {
      // comment: tell players 'you can place your ships using this ID
    }
  }

  "The game" should {
    "pick a random player to start" in {

    }
    "alternate player turns after the initial move" in {

    }
    "identify each player when she or he makes a turn" in {

    }
    "notify both players if someone won" in {

    }
    "notify the players on the results of the actions of the players"
  }

  "place ships" should {
    "place all ships for a player" in {

    }
    "return an error for invalid input" in {

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
