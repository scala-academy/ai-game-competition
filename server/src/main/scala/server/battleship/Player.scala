package server.battleship

import server.battleship.GridImpl.ShipPlacement
import server.battleship.GridImpl.ShipPlacement.Direction.{HORIZONTAL, VERTICAL}

import scala.io.StdIn
import scala.util.{Failure, Random, Success, Try}


trait Player {
  val shipPlacements: Set[ShipPlacement]

  def getAttack: (Int, Char)

  def processAttackResult(attackResult: AttackResult): Unit = ()
}

object HumanInterface {
  def addShips: Set[ShipPlacement] = {

    println("Hello. Please give us your input in the format row column H/V (e.g. \"1 A V\"")

    val sortedShips = Ship.allShips.toSeq.sortBy(ship => (-ship.size, ship.name))
    getShipPlacements(sortedShips)
  }

  private[battleship] def getShipPlacements(todo: Seq[Ship], acc: Set[ShipPlacement] = Set.empty): Set[ShipPlacement] =
    todo match {
      case Nil =>
        acc
      case ship +: ships =>
        val input = StdIn.readLine(s"${ship.name}: ")
        val Array(row, col, direction) = input.split(" ")

        Try(ShipPlacement(ship, row.toInt, col.charAt(0), direction == "H")) match {
          case Success(shipPlacement) =>
            getShipPlacements(ships, acc + shipPlacement)
          case Failure(_: IllegalArgumentException) =>
            println(s"Incorrect placement: $input")
            getShipPlacements(todo, acc)
          case Failure(_) =>
            println(s"Garbage input: $input")
            getShipPlacements(todo, acc)
        }
    }
}

class HumanInterface(val shipPlacements: Set[ShipPlacement] =
                 DummyAI.shipPlacements - DummyAI.carrierP + DummyAI.carrierP.copy(col = 'B')) extends Player {

  private var attackResults: Map[(Int, Char), AttackResult] = Map.empty
  private[battleship] var lastAttack: (Int, Char) = _

  override def getAttack: (Int, Char) = {
    val Array(row, column) = StdIn.readLine("Define your attack point row column ").split(" ")
    lastAttack = (row.toInt, column.charAt(0))
    lastAttack
  }

  override def processAttackResult(attackResult: AttackResult): Unit = {
    println(" Message: " + attackResult.getMessage)
    attackResults = attackResults + (lastAttack -> attackResult)
  }

  def showAttackGrid(): Unit = {
    val locations = for {
      r <- 1 to GridImpl.size
      c <- 'A' until ('A' + GridImpl.size).toChar
    } yield {
      attackResults.get((r, c)) match {
        case None => '.'
        case Some(Miss) => '~'
        case Some(Hit) => 'X'
        case Some(Sunk(ship)) => ship.symbol
        case Some(Win) => 'W'
      }
    }
    val slided = locations.sliding(10, 10).map(_.mkString(" ")).toSeq
    println(slided.mkString("\n"))
  }

}

object DummyAI extends Player {

  val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
  val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
  val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
  val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
  val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

  override val shipPlacements: Set[ShipPlacement] = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)

  override def getAttack: (Int, Char) = (1, 'A')
}

object RandomAttackAI extends Player {
  val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
  val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
  val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
  val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
  val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

  override val shipPlacements: Set[ShipPlacement] = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)

  override def getAttack: (Int, Char) = {
    val random = new Random()
    val row = 1 + random.nextInt(10)
    val col = (random.nextInt(10)+'A').toChar
    (row, col)
  }
}
