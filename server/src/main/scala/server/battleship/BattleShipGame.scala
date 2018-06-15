package server.battleship

import scala.util.Random

object BattleShipGame {
  val rnd = new Random(556)
  def create(player1: Player, player2: Player): BattleShipGame = {
    if(rnd.nextBoolean()) BattleShipGame(player1, player2)
    else BattleShipGame(player2, player1)
  }
}

case class BattleShipGame(playerOnTurn: Player, opponent: Player) {
  val grid1 = Grid(playerOnTurn.shipPlacements)
  val grid2 = Grid(opponent.shipPlacements)

}
