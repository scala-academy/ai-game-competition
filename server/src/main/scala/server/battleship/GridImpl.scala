package server.battleship

object GridImpl {

  val size = 10

  object ShipPlacement {
    type Direction = Boolean

    object Direction {
      val HORIZONTAL = true
      val VERTICAL = false
    }

  }

  //TODO: Create a type alias or case class with a row and col

  case class ShipPlacement(ship: Ship, point: Point, isHorizontal: Boolean, hits: Set[Point] = Set()) {
    require({
      val leftCorrect = point.col >= 'A'
      val rightCorrect =
        if (isHorizontal) point.col + ship.size <= 'A' + size
        else point.col <= 'A' + size
      leftCorrect && rightCorrect
    }, "Column out of boundaries")
    require({
      val upCorrect = point.row >= 1
      val downCorrect =
        if (isHorizontal) point.row <= size
        else point.row + ship.size <= 1 + size
      upCorrect && downCorrect
    }, "Row out of boundaries")
    require(hits.forall(isHit), "Invalid hit point(s)")


    lazy val getPositionPoints: Set[Point] = {

      (0 until ship.size).map(index => {
        if (isHorizontal) point.copy(col=(point.col + index).toChar)
        else point.copy(row = point.row + index)
      }).toSet
    }

    def isHit(hitPoint: Point): Boolean = {
      getPositionPoints.contains(hitPoint)
    }

    def hit(hitPoint: Point): ShipPlacement = {

      this.copy(hits = this.hits + hitPoint)
    }


    def isSunk = hits.size == ship.size
  }

}

trait Grid {
  def attack(point: Point): (Grid, AttackResult)
  def shipPlacements: Set[GridImpl.ShipPlacement]

  def gridAsRowStrings: Seq[String] = {
    val locations = for {
      r <- 1 to GridImpl.size
      c <- 'A' until ('A' + GridImpl.size).toChar
    } yield {
      shipPlacements.find(_.getPositionPoints.contains(Point(r, c))) match {
        case None => "-"
        case Some(shipPlacement) if !shipPlacement.hits.contains(Point(r, c)) => shipPlacement.ship.symbol
        case _ => "X"
      }
    }
    locations.sliding(10, 10).map(_.mkString(" ")).toSeq
  }

}

case class GridImpl(shipPlacements: Set[GridImpl.ShipPlacement]) extends Grid {
  require(shipPlacements.size == 5, "Ship number is incorrect")
  require(shipPlacements.map(_.ship.name) == Ship.allShipTypes, "Not all types of ship are present")
  require(overlaps.isEmpty, "Ships overlap at point(s) " + overlaps.mkString(", "))

  private def overlaps: Set[Point] = {
    val points = shipPlacements.map(_.getPositionPoints).toList.flatten
    points
    .groupBy(identity)
    .collect { case (x, _ :: _ :: _) => x }
    .toSet
  }

  override def attack(point: Point): (Grid, AttackResult) = {
    shipPlacements.find(sp => sp.isHit(point)) match {
      case None =>
        (this, Miss)

      case Some(shipP) =>
        val newHit = point
        val newShipP = shipP.copy(hits = shipP.hits + newHit)
        val newGrid = GridImpl(shipPlacements - shipP + newShipP)
        val attackResult =
          if (newGrid.isGameOver) Win
          else if (newShipP.isSunk) Sunk(newShipP.ship)
          else Hit
        (newGrid, attackResult)
    }
  }

  def isGameOver = shipPlacements.forall(_.isSunk)

}
