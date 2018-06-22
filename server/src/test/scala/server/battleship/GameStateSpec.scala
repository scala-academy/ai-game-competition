package server.battleship

import org.scalatest.{Matchers, WordSpec}

class GameStateSpec extends WordSpec with Matchers {

  val mockPlayer1 = new Player{
    override val shipPlacements = Human.shipPlacements
    override def getAttack = (3,'C')
  }

  val mockPlayer2 = new Player{
    override val shipPlacements = Artificial.shipPlacements
    override def getAttack = Human.shipPlacements.iterator.next().getPositionPoints.iterator.next()
  }

  val gameState = GameState.create(mockPlayer1, mockPlayer2)

  "initiate" should {
    "have a grid for each player" in {
      gameState.playerOnTurn shouldBe mockPlayer1
      gameState.opponent shouldBe mockPlayer2

      gameState.grid1.shipPlacements shouldBe mockPlayer1.shipPlacements
      gameState.grid2.shipPlacements shouldBe mockPlayer2.shipPlacements
    }
  }

  "The game" should {
    "pick a random player to start" in {
      val startingPlayers = for(i <- 1 to 10) yield GameState.create(mockPlayer1, mockPlayer2).playerOnTurn
      startingPlayers.toSet.size shouldBe 2
    }
    "alternate player turns after the initial move" in {
      val currentPlayer = gameState.playerOnTurn
      val newGame : GameState = gameState.move._1
      newGame.playerOnTurn should not be currentPlayer

    }
    "grids should be updated with the move of a player" in {
      val newGame = gameState.move._1
      //It misses
      newGame.grid1 shouldBe gameState.grid2
      //Gets a hit
      val newGame2 = newGame.move._1
      newGame2.grid1 should not be newGame.grid2
    }
    "notify both players if someone won" in {
      val positions = gameState.grid1.shipPlacements.flatMap(_.getPositionPoints).toSeq
      val almostFinishedGrid = positions.tail.foldLeft(gameState.grid1){case (grid, (row, col)) => grid.attack(row, col)._1}
      val mockPlayer = new Player {
        override def getAttack: (Int, Char) = {positions.head}
        override val shipPlacements: Set[Grid.ShipPlacement] = Human.shipPlacements
      }

      val newGame = GameState(mockPlayer, gameState.grid1, mockPlayer2, almostFinishedGrid)
      val (finishedGame, message) = newGame.move
      message shouldBe Win
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
