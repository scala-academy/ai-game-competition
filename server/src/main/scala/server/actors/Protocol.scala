package server.actors

import server.battleship.AttackResult

object Protocol {

  /**
    * Game -> Player
    */
  case object GetAttack

  /**
    * Game -> Player
    */
  case class ProcessAttackResult(result: AttackResult)

  /**
    * Player -> Game
    */
  case class Attack(row: Int, col: Char)

}
