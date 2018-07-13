package server.battleship

object Ship {
  val carrier = Ship(5, "Carrier")
  val battleShip = Ship(4, "Battleship")
  val cruiser = Ship(3, "Cruiser")
  val submarine = Ship(3, "Submarine")
  val destroyer = Ship(2, "Destroyer")

  val allShips = Set(carrier, battleShip, cruiser, submarine, destroyer)
  val allShipTypes = allShips.map(_.name)
}

case class Ship private(size: Int, name: String) {
  def symbol =
    if (this == Ship.cruiser) 'c'
    else name.charAt(0)
}
