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

      val carrierP = GridImpl.ShipPlacement(Ship.carrier, Point(1, 'A'), HORIZONTAL)
      val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, Point(4, 'D'), VERTICAL)
      val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, Point(2, 'H'), HORIZONTAL)
      val submarineP = GridImpl.ShipPlacement(Ship.submarine, Point(10, 'H'), HORIZONTAL)
      val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, Point(9, 'A'), VERTICAL)

      val expected = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)


      val input = "1 A H\n4 D V\n2 H H\n10 H H\n9 A V"

      mockStdIn(input, {
        val shipPlacements = Human.addShips
        shipPlacements shouldBe expected
      })

    }

    "Pick ship placements with incorrect input" in {
      val carrierP = GridImpl.ShipPlacement(Ship.carrier, Point(1, 'A'), HORIZONTAL)

      val expected = Set(carrierP)

      val input = "111 A H\n1 A H"

      mockStdIn(input, {
        val shipPlacements = Human.getShipPlacements(Seq(Ship.carrier))
        shipPlacements shouldBe expected
      })

    }

  }

  "A player" should {
    "Provide attack locations" in {

      val input = "1 A"
      mockStdIn(input, {
        Human().getAttack shouldBe Point(1, 'A')
      })

    }

    "Receive notifications" in {

    }
  }

}
