package server.battleship

import scala.util.Random

object GameState {
  val rnd = new Random(556)
  def create(player1: Player, player2: Player): GameState = {
    val grid1 = GridImpl(player1.shipPlacements)
    val grid2 = GridImpl(player2.shipPlacements)
    if(rnd.nextBoolean()) GameState(player1, grid1, player2, grid2)
    else GameState(player2, grid2, player1, grid1)
  }
}

case class GameState private[battleship](playerOnTurn: Player, grid1: Grid, opponent: Player, grid2: Grid) {

  def move: (GameState, AttackResult) = {
    val (row, col) = playerOnTurn.getAttack
    val (newGrid2, message) = grid2.attack(row, col)
    (GameState(opponent, newGrid2, playerOnTurn, grid1), message)
  }
}