package server.battleship

import server.battleship.GridImpl.ShipPlacement
import server.battleship.GridImpl.ShipPlacement.Direction.{HORIZONTAL, VERTICAL}

import scala.io.StdIn


trait Player {
  val shipPlacements: Set[ShipPlacement]

  def getAttack: (Int, Char)
}

object Human {
  def addShips(): Set[ShipPlacement] = {
    //TODO make this pretty
    println("Hello. Please give us your input in the format row column H/V")
    val Array(caRow, caCol, caDirection) = StdIn.readLine("Carrier: ").split(" ")
    val Array(baRow, baCol, baDirection) = StdIn.readLine("Battleship: ").split(" ")
    val Array(crRow, crCol, crDirection) = StdIn.readLine("Cruiser: ").split(" ")
    val Array(suRow, suCol, suDirection) = StdIn.readLine("Submarine: ").split(" ")
    val Array(deRow, deCol, deDirection) = StdIn.readLine("Destroyer: ").split(" ")

    Set(
      ShipPlacement(Ship.carrier, caRow.toInt, caCol.charAt(0), caDirection == "H"),
      ShipPlacement(Ship.battleShip, baRow.toInt, baCol.charAt(0), baDirection == "H"),
      ShipPlacement(Ship.cruiser, crRow.toInt, crCol.charAt(0), crDirection == "H"),
      ShipPlacement(Ship.submarine, suRow.toInt, suCol.charAt(0), suDirection == "H"),
      ShipPlacement(Ship.destroyer, deRow.toInt, deCol.charAt(0), deDirection == "H")
    )
  }
}

case class Human(shipPlacements: Set[ShipPlacement] =
                 Artificial.shipPlacements - Artificial.carrierP + Artificial.carrierP.copy(col = 'B')) extends Player {

  override def getAttack: (Int, Char) = {
    val Array(row, column) = StdIn.readLine("Define your attack point row column ").split(" ")
    (row.toInt, column.charAt(0))
  }

}

object Artificial extends Player {

  val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
  val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
  val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
  val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
  val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

  override val shipPlacements: Set[ShipPlacement] = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)

  override def getAttack: (Int, Char) = ???
}
