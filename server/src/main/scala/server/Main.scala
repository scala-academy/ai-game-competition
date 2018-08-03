package server

import server.battleship.BattleshipGame

object Main extends App {
  //val defaultPort = 8080

  //Await.ready(Server(defaultPort).server)

  val game = BattleshipGame.createDefaultGame

  game.playGameTillEnd(game.initialState)

}
