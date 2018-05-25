package server.battleship

import server.battleship.Grid.ShipPlacement

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

  val size = 10

  case class ShipPlacement(ship: Ship, row: Int, col: Char, isHorizontal: Boolean, hits: Set[(Int, Char)]) {
    require({
      val leftCorrect = col >= 'A'
      val rightCorrect =
        if (isHorizontal) col + ship.size <= 'A' + size
        else col <= 'A' + size
      leftCorrect && rightCorrect
    }, "Column out of boundaries")
    require({
      val upCorrect = row >= 1
      val downCorrect =
        if (isHorizontal) row <= size
        else row + ship.size <= 1 + size
      upCorrect && downCorrect
    }, "Row out of boundaries")


    def isSunk = ???
  }

}

case class Grid(shipPlacements: Set[Grid.ShipPlacement]) {
  require(shipPlacements.size == 5, "Shipment number is incorect: " + shipPlacements.size + " instead of 5")
  require(shipPlacements.map(_.ship.name) == Ship.allShipTypes, "Not all types in ship placements")


  def attack(row: Int, col: Char): (Grid, Option[Message]) = {
    ???
  }

}
