package server.battleship

object BattleshipGame {

  def createDefaultGame: BattleshipGame = {
    val human = Human(Human.addShips)
    val opponent = DummyAI
    BattleshipGame(human, opponent)
  }
}

case class BattleshipGame(player1: Player, player2: Player) {

  val initialState: GameState = GameState.create(player1, player2)

  def playGameTillEnd(gameState: GameState): Unit = {
    println(gameState.gameStateAsString)
    val playerOnTurn = gameState.playerOnTurn
    val (row, col) = playerOnTurn.getAttack
    val (newGameState, attackResult) = gameState.processMove(row, col)
    playerOnTurn.processAttackResult(attackResult)

    if(attackResult == Win) ()
    else playGameTillEnd(newGameState)
  }

}
