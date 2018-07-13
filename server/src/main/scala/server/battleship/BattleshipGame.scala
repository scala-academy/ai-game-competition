package server.battleship

object BattleshipGame extends App {

  val human = Human(Human.addShips)
  val opponent = Artificial

  val humanGrid = GridImpl(human.shipPlacements)
  val opponentGrid = GridImpl(opponent.shipPlacements)

  val initialState = GameState(human, humanGrid, opponent, opponentGrid)

  playGameTillEnd(initialState)

  def playGameTillEnd(gameState: GameState): Unit = {
    val playerOnTurn = gameState.playerOnTurn
    val (row, col) = playerOnTurn.getAttack
    val (newGameState, attackResult) = gameState.processMove(row, col)
    if(attackResult == Win) ()
    else playGameTillEnd(newGameState)
  }

}
