package server.battleship

sealed trait Message

case object YouBeatMe extends Message

case class YouSankMy(ship: Ship) extends Message

object Grid {

  object ShipPlacement {
    type Direction = Boolean

    object Direction {
      val HORIZONTAL = true
      val VERTICAL = false
    }

  }

  case class ShipPlacement(ship: Ship, row: Int, col: Char, isHorizontal: Boolean, hits: Set[(Int, Char)]) {

    def isSunk = ???
  }

}

case class Grid(shipPlacements: Set[Grid.ShipPlacement]) {
  require(shipPlacements.size == 5, "Shipment number is incorect: " + shipPlacements.size + " instead of 5")
  require(shipPlacements.map(_.ship.name) == Ship.allShipTypes, "Not all types in ship placements")
  require(shipPlacements.filter(s => s.col))


  def attack(row: Int, col: Char): (Grid, Option[Message]) = {
    ???
  }




}
