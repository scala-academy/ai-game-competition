package server.battleship

import java.io.{ByteArrayInputStream, OutputStream}

import org.scalatest.{Matchers, WordSpec}
import server.battleship.GridImpl.ShipPlacement.Direction.{HORIZONTAL, VERTICAL}


class PlayerSpec extends WordSpec with Matchers {
  val nullOut = new OutputStream {
    override def write(b: Int): Unit = ()
  }

  def mockStdIn(input: String, body: => Unit): Unit = {
    val in = new ByteArrayInputStream(input.getBytes)
    Console.withIn(in) {
      Console.withOut(nullOut) {
        body
      }
    }
  }

  "A Human player" should {

    "Pick ship placements" in {

      val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
      val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
      val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
      val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
      val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

      val expected = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)


      val input = "1 A H\n4 D V\n2 H H\n10 H H\n9 A V"

      mockStdIn(input, {
        val shipPlacements = Human.addShips
        shipPlacements shouldBe expected
      })

    }

  }

  "A player" should {
    "Provide attack locations" in {

      val input = "1 A"
      mockStdIn(input, {
        Human().getAttack shouldBe(1, 'A')
      })

    }

    "Receive notifications" in {

    }
  }

}
