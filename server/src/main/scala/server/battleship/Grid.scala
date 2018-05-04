package server.battleship

sealed trait Message

case object YouBeatMe extends Message

case class YouSankMy(ship: Ship) extends Message

object Grid {
  case class ShipPlacement(row: Int, col: Char, isHorizontal: Boolean)
}

case class Grid(shipPlacements: Grid.ShipPlacement) {

  def attack(row: Int, col: Char): (Grid, Option[Message]) = {
    ???
  }

  /**
    * @param placement Topleft (row, col, isHorizontal) of the location the ship is to be placed
    * @param ship The ship to be placed
    * @return A grid if the placement is valid (not overlapping with other ships, not out of boundaries)
    */
  def placeShip(placement: Grid.ShipPlacement, ship: Ship): Option[Grid] = ???

}
