package server.battleship

import org.scalatest.{Matchers, WordSpec}
import Grid.ShipPlacement.Direction._
import Grid.AttackResult._

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

  val carrierP = Grid.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL, Set())
  val battleShipP = Grid.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL, Set())
  val cruiserP = Grid.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL, Set())
  val submarineP = Grid.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL, Set())
  val destroyerP = Grid.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL, Set())

  val correctShipPlacements = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)


  "Grid" should {
    "return a new grid, if the 5 ships positions are correct" in {
      val _ = Grid(correctShipPlacements)
    }

    "should return error if ship number is wrong" in {
      val exceptionInsufficientNumber = intercept[IllegalArgumentException] {
        //        val wrongPlacement = correctShipPlacements.filter(sp => sp.ship != carrier)
        val wrongPlacement = correctShipPlacements - carrierP
        val _ = Grid(wrongPlacement)
      }
      assert(exceptionInsufficientNumber.getMessage === "requirement failed: Ship number is incorrect")

      val exceptionExceedingNumber = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements + destroyerP.copy(row = 6, col = 'G')
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
        val wrongPlacement = correctShipPlacements - carrierP + carrierP.copy(row = 5)
        val _ = Grid(wrongPlacement)
      }
      assert(exception.getMessage === "requirement failed: Ships overlap")

    }


    "Fails to create Grid if group of ships is missing ship types" in {
      val exception = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements - cruiserP + destroyerP.copy(row = 6, col = 'G')
        val _ = Grid(wrongPlacement)
      }
      assert(exception.getMessage === "requirement failed: Not all types of ship are present")
    }
  }

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
  "attack" should {
    /*    A B C D E F G H I J
        1 # # ! # # * * * * *
        2 ~ * * * * * * # # #
        3 * * * * * * * * * *
        4 * * * # * * * * * *
        5 * * * # * * * * * *
        6 * * * # * * * * * *
        7 * * * # * * * * * *
        8 * * * * * * * * * *
        9 # * * * * * * * * *
       10 # * * * * * * # # #
    */
    "return a new grid and Hit if attack is successful" in {
      val hitPoint = (1, 'C')
      val grid = Grid(correctShipPlacements)
      val (newGrid, hitOrMiss) = grid.attack(hitPoint._1, hitPoint._2)
      hitOrMiss shouldBe HIT
      newGrid.shipPlacements.find(sp => sp.ship == Ship.carrier).get.hits should contain(hitPoint)
    }

    "return the same grid and Miss if attack is unsuccessful" in {
      val hitPoint = (2, 'A')
      val grid = Grid(correctShipPlacements)
      val (sameGrid, hitOrMiss) = grid.attack(hitPoint._1, hitPoint._2)
      hitOrMiss shouldBe MISS
      sameGrid shouldEqual grid
    }

    "return a new grid and 'You sank my Battleship' when a battleship is completely hit" in {
      //TODO

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
        val _ = carrierP.copy(col = ('A' + Grid.size + 1).toChar)
      }
      assert(thrown.getMessage === "requirement failed: Column out of boundaries")

    }

    "fail to create ShipPlacement, if the shipPlacement column is a special character" in {
      val thrown = intercept[IllegalArgumentException] {
        val _ = carrierP.copy(col = '$')
      }
      assert(thrown.getMessage === "requirement failed: Column out of boundaries")

    }

    "fail to create ShipPlacement, if the shipPlacement row is outside the Grid" in {
      val thrown = intercept[IllegalArgumentException] {
        val _ = carrierP.copy(row = Grid.size + 1)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")
    }

    "fail to create ShipPlacement, if the shipPlacement row is a negative value" in {
      val thrown = intercept[IllegalArgumentException] {
        val _ = carrierP.copy(row = -1)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")
    }

    "fail to create ShipPlacement, if the shipPlacement vertical with ship out of boundaries" in {
      val thrown = intercept[IllegalArgumentException] {
        val _ = carrierP.copy(row = 10, isHorizontal = false)
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
        val _ = carrierP.copy(row = 7, isHorizontal = false)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")

    }

    "findPositionPoints should return the correct points" in {
      val carrierPoints = carrierP.getPositionPoints
      val carrierExpected = Set((1, 'A'), (1, 'B'), (1, 'C'), (1, 'D'), (1, 'E'))
      carrierExpected shouldEqual carrierPoints

      val destroyerPoints = destroyerP.getPositionPoints
      val destroyerExpected = Set((9, 'A'), (10, 'A'))
      destroyerExpected shouldEqual destroyerPoints
    }

  }

}
