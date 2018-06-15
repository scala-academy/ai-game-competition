package server.battleship

sealed trait AttackResult {

  def getMessage: String
}


case object Hit extends AttackResult {

  val getMessage = "You hit my ship!"
}

case class Sunk(ship: Ship) extends AttackResult {

  def getMessage = s"You sunk my ${ship.name}!"
}

case object Miss extends AttackResult {

  val getMessage = "You miss"
}

case object Win extends AttackResult {
  val getMessage = "You win!"
}
