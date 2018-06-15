package server.battleship

import server.battleship.Grid.ShipPlacement
import server.battleship.Grid.ShipPlacement.Direction.{HORIZONTAL, VERTICAL}


trait Player {
  val shipPlacements : Set[ShipPlacement]
}


object Human extends Player {
  override val shipPlacements: Set[ShipPlacement] = Artificial.shipPlacements
}
object Artificial extends Player {

  val carrierP = Grid.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
  val battleShipP = Grid.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
  val cruiserP = Grid.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
  val submarineP = Grid.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
  val destroyerP = Grid.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

  override val shipPlacements: Set[ShipPlacement] = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)
}
