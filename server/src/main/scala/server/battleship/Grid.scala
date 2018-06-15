package server.battleship

object Grid {

  val size = 10

  object ShipPlacement {
    type Direction = Boolean

    object Direction {
      val HORIZONTAL = true
      val VERTICAL = false
    }

  }

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
    require(hits.forall(isHit), "Invalid hit point(s)")


    lazy val getPositionPoints: Set[(Int, Char)] = {
      (0 until ship.size).map(index => {
        if (isHorizontal) (row, (col + index).toChar)
        else (row + index, col)
      }).toSet
    }

    def isHit(hitPoint: (Int, Char)): Boolean = {
      getPositionPoints.contains(hitPoint)
    }

    def hit(hitPoint: (Int, Char)): ShipPlacement = {

      this.copy(hits = this.hits + hitPoint)
    }


    def isSunk = hits.size == ship.size
  }

}

case class Grid(shipPlacements: Set[Grid.ShipPlacement]) {
  require(shipPlacements.size == 5, "Ship number is incorrect")
  require(shipPlacements.map(_.ship.name) == Ship.allShipTypes, "Not all types of ship are present")
  require(overlaps.isEmpty, "Ships overlap at point(s) " + overlaps.toString)

  lazy val overlaps: Set[(Int, Char)] = {
    val points = shipPlacements.map(_.getPositionPoints).toList.flatten
    points
    .groupBy(identity)
    .collect { case (x, _ :: _ :: _) => x }
    .toSet
  }

  def attack(row: Int, col: Char): (Grid, AttackResult) = {
    shipPlacements.find(sp => sp.isHit((row, col))) match {
      case None =>
        (this, Miss)

      case Some(shipP) =>
        val newHit = (row, col)
        val newShipP = shipP.copy(hits = shipP.hits + newHit)
        val newGrid = Grid(shipPlacements - shipP + newShipP)
        val attackResult =
          if (newGrid.isGameOver) Win
          else if (newShipP.isSunk) Sunk(newShipP.ship)
          else Hit
        (newGrid, attackResult)
    }
  }

  def isGameOver = shipPlacements.forall(_.isSunk)

}
