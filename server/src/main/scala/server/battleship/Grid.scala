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

  val size = 10

  case class ShipPlacement(ship: Ship, row: Int, col: Char, isHorizontal: Boolean, hits: Set[(Int, Char)] = Set()) {
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


    def getPositionPoints(): Set[(Int, Char)] = {
      (0 until ship.size).map(index => {
        if (isHorizontal) (row, (col + index).toChar)
        else (row + index, col)
      }).toSet
    }


    def isSunk = hits.size == ship.size
  }

}

case class Grid(shipPlacements: Set[Grid.ShipPlacement]) {
  require(shipPlacements.size == 5, "Ship number is incorrect")
  require(shipPlacements.map(_.ship.name) == Ship.allShipTypes, "Not all types of ship are present")
  require(nonOverlapping(), "Ships overlap")





  def attack(row: Int, col: Char): (Grid, Option[Message]) = {
    ???
  }

  def nonOverlapping(): Boolean = {
    val points = shipPlacements.map(_.getPositionPoints()).toList.flatten
    points.distinct.lengthCompare(points.size) == 0
  }

}
