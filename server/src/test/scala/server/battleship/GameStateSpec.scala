package server.battleship

import org.scalatest.{Matchers, WordSpec}

class GameStateSpec extends WordSpec with Matchers {

  val mockPlayer1 = new Player {
    override val shipPlacements = Human().shipPlacements

    override def getAttack = (3, 'C')
  }

  val mockPlayer2 = new Player {
    override val shipPlacements = Artificial.shipPlacements

    override def getAttack = Human().shipPlacements.iterator.next().getPositionPoints.iterator.next()
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
      val startingPlayers = for (i <- 1 to 10) yield GameState.create(mockPlayer1, mockPlayer2).playerOnTurn
      startingPlayers.toSet.size shouldBe 2
    }
    "alternate player turns after the initial move" in {
      val (row, col) = gameState.playerOnTurn.getAttack
      val newGame: GameState = gameState.processMove(row, col)._1
      newGame.playerOnTurn should not be gameState.playerOnTurn

    }
    "grids should be updated with the move of a player" in {
      val (missRow, missCol) = gameState.playerOnTurn.getAttack
      val newGame = gameState.processMove(missRow, missCol)._1
      //It misses
      newGame.grid1 shouldBe gameState.grid2
      //Gets a hit
      val (hitRow, hitCol) = newGame.playerOnTurn.getAttack
      val newGame2 = newGame.processMove(hitRow, hitCol)._1
      newGame2.grid1 should not be newGame.grid2
    }
    "notify both players if someone won" in {
      val positions = gameState.grid1.shipPlacements.flatMap(_.getPositionPoints).toSeq
      val almostFinishedGrid = positions.tail.foldLeft(gameState.grid1) { case (grid, (row, col)) => grid.attack(row, col)._1 }
      val mockPlayer = new Player {
        override def getAttack: (Int, Char) = {
          positions.head
        }

        override val shipPlacements: Set[GridImpl.ShipPlacement] = Human().shipPlacements
      }

      val newGame = GameState(mockPlayer, gameState.grid1, mockPlayer2, almostFinishedGrid)
      val (row, col) = newGame.playerOnTurn.getAttack
      val (_, message) = newGame.processMove(row, col)
      message shouldBe Win
    }
    "notify the player on the results of their actions" in {
      def mockGrid(attackResult: AttackResult): Grid = new Grid {
        override def attack(row: Int, col: Char): (Grid, AttackResult) = (gameState.grid1, attackResult)
        override val shipPlacements: Set[GridImpl.ShipPlacement] = Set.empty
      }

      val attackResult = Set(Hit, Miss, Sunk(Ship.battleShip))

      attackResult.foreach(ar => {
        val game = GameState(mockPlayer1, mockGrid(ar), mockPlayer1, mockGrid(ar))
        val (row, col) = game.playerOnTurn.getAttack
        val (_, missResult) = game.processMove(row, col)
        missResult shouldBe ar
      })
    }
  }

  "gameStateAsString" should {
    "return the two grids as a pretty string" in {
      println(gameState.gameStateAsString)
    }
  }

}
