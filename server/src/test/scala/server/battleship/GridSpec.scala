package server.battleship

import org.scalatest.{Matchers, WordSpec}
import Grid.ShipPlacement.Direction._

class GridSpec extends WordSpec with Matchers {

  /*    A B C D E F G H I J
      1 # # # # # * * * * *
      2 * * * * * * * # # #
      3 * * * * * * * * * *
      4 * * * # * * * * * *
      5 * * * # * * * * * *
      6 * * * # * * * * * *
      7 * * * # * * * * * *
      8 * * * * * * * * * *
      9 # * * * * * * * * *
     10 # * * * * * * # # #
  */

  val carrier = Grid.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL, Set())
  val battleShip = Grid.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL, Set())
  val cruiser = Grid.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL, Set())
  val submarine = Grid.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL, Set())
  val destroyer = Grid.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL, Set())

  val correctShipPlacements = Set(carrier, battleShip, cruiser, submarine, destroyer)


  "Grid" should {
    "return a new grid, if the 5 ships positions are correct" in {
      val _ = Grid(correctShipPlacements)
    }

    "should return error if ship number is wrong" in {
      val exceptionInsufficientNumber = intercept[IllegalArgumentException] {
        //        val wrongPlacement = correctShipPlacements.filter(sp => sp.ship != carrier)
        val wrongPlacement = correctShipPlacements - carrier
        val _ = Grid(wrongPlacement)
      }
      assert(exceptionInsufficientNumber.getMessage === "requirement failed: Ship number is incorrect")

      val exceptionExceedingNumber = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements + destroyer.copy(row = 6, col = 'G')
        val _ = Grid(wrongPlacement)
      }
      assert(exceptionExceedingNumber.getMessage === "requirement failed: Ship number is incorrect")
    }


    /*  A B C D E F G H I J
      1 * * * * * * * * * *
      2 * * * * * * * # # #
      3 * * * * * * * * * *
      4 * * * # * * * * * *
      5 # # # X # * * * * *
      6 * * * # * * * * * *
      7 * * * # * * * * * *
      8 * * * * * * * * * *
      9 # * * * * * * * * *
     10 # * * * * * * # # #
  */
    "Fails to create Grid if one ship is on top of the other" in {
      val exception = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements - carrier + carrier.copy(row = 5)
        val _ = Grid(wrongPlacement)
      }
      assert(exception.getMessage === "requirement failed: Ships overlap")

    }


    "Fails to create Grid if group of ships is missing ship types" in {
      val exception = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements - cruiser + destroyer.copy(row = 6, col = 'G')
        val _ = Grid(wrongPlacement)
      }
      assert(exception.getMessage === "requirement failed: Not all types of ship are present")
    }
  }

  "attack" should {
    "return a new grid and Hit or Miss" in {

    }
    "return a new grid and 'You sank my Battleship' when a battleship is completely hit" in {

    }
    "return a new grid and 'You beat me' when the last ship is completely hit" in {

    }
  }


  "ShipPlacement" should {
    /*    A B C D E F G H I J
        1 * * * * * * * * * *
        2 * * * * * * * * * *
        3 * * * * * * * * * *
        4 * * * * * * * * * *
        5 * * * * * * * * * *
        6 * * * * * * * * * *
        7 * * * * * * * * * *
        8 * * * * * * * * * *
        9 * * * * * * * * * *
       10 * * * * * # # # # #
    */
    "creates object, if the shipPlacement is inside the Grid" in {
      val _ = Grid.ShipPlacement(Ship.carrier, 10, 'F', HORIZONTAL)
    }


    /*    A B C D E F G H I J
        1 * * * * * * * * * #
        2 * * * * * * * * * #
        3 * * * * * * * * * #
        4 * * * * * * * * * #
        5 * * * * * * * * * #
        6 * * * * * * * * * *
        7 * * * * * * * * * *
        8 * * * * * * * * * *
        9 * * * * * * * * * *
       10 * * * * * * * * * *
    */
    "create object, if the vertical shipPlacement is inside the Grid" in {
      val _ = Grid.ShipPlacement(Ship.carrier, 1, 'J', VERTICAL)
    }

    "fail to create ShipPlacement, if the shipPlacement column is outside the Grid" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(col = ('A' + Grid.size + 1).toChar)
      }
      assert(thrown.getMessage === "requirement failed: Column out of boundaries")

    }

    "fail to create ShipPlacement, if the shipPlacement column is a special character" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(col = '$')
      }
      assert(thrown.getMessage === "requirement failed: Column out of boundaries")

    }

    "fail to create ShipPlacement, if the shipPlacement row is outside the Grid" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(row = Grid.size + 1)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")
    }

    "fail to create ShipPlacement, if the shipPlacement row is a negative value" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(row = -1)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")
    }

    "fail to create ShipPlacement, if the shipPlacement vertical with ship out of boundaries" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(row = 10, isHorizontal = false)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")

    }

    /*    A B C D E F G H I J
        1 * * * * * * * * * *
        2 * * * * * * * * * *
        3 * * * * * * * * * *
        4 * * * * * * * * * *
        5 * * * * * * * * * *
        6 * * * * * * * * * *
        7 # * * * * * * * * *
        8 # * * * * * * * * *
        9 # * * * * * * * * *
       10 # * * * * * * * * *
          #
    */
    "fail to create ShipPlacement, if the shipPlacement vertical with ship out of boundaries TEST" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(row = 7, isHorizontal = false)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")

    }

    "findPositionPoints should return the correct points" in {
      val carrierPoints = carrier.getPositionPoints()
      val carrierExpected = Set((1,'A'), (1,'B'), (1,'C'),(1,'D'), (1,'E'))
      carrierExpected shouldEqual carrierPoints

      val destroyerPoints = destroyer.getPositionPoints()
      val destroyerExpected = Set((9,'A'), (10,'A'))
      destroyerExpected shouldEqual destroyerPoints
    }

  }

}
