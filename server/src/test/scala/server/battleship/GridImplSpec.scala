package server.battleship

import org.scalatest.{Matchers, WordSpec}
import GridImpl.ShipPlacement.Direction._
import server.battleship.GridImpl.ShipPlacement

class GridImplSpec extends WordSpec with Matchers {

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

  val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
  val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
  val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
  val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
  val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

  val correctShipPlacements = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)


  "Grid" should {
    "return a new grid, if the 5 ships positions are correct" in {
      val _ = GridImpl(correctShipPlacements)
    }

    "should return error if ship number is wrong" in {
      val exceptionInsufficientNumber = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements - carrierP
        val _ = GridImpl(wrongPlacement)
      }
      assert(exceptionInsufficientNumber.getMessage === "requirement failed: Ship number is incorrect")

      val exceptionExceedingNumber = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements + destroyerP.copy(row = 6, col = 'G')
        val _ = GridImpl(wrongPlacement)
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
        val _ = GridImpl(wrongPlacement)
      }
      exception.getMessage shouldBe "requirement failed: Ships overlap at point(s) (5,D)"

    }


    "Fails to create Grid if group of ships is missing ship types" in {
      val exception = intercept[IllegalArgumentException] {
        val wrongPlacement = correctShipPlacements - cruiserP + destroyerP.copy(row = 6, col = 'G')
        val _ = GridImpl(wrongPlacement)
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
      val grid = GridImpl(correctShipPlacements)
      val (newGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      attackResult shouldBe Hit
      newGrid.shipPlacements.find(sp => sp.ship == Ship.carrier) match {
        case Some(s) => s.hits should contain(hitPoint)
        case _ => fail()
      }

    }

    "return the same grid and Miss if attack is unsuccessful" in {
      val hitPoint = (2, 'A')
      val grid = GridImpl(correctShipPlacements)
      val (sameGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      attackResult shouldBe Miss
      sameGrid shouldEqual grid
    }

    /*   A B C D E F G H I J
       1 # # # # # * * * * *
       2 * * * * * * * # # #
       3 * * * * * * * * * *
       4 * * * # * * * * * *
       5 * * * # * * * * * *
       6 * * * # * * * * * *
       7 * * * # * * * * * *
       8 * * * * * * * * * *
       9 # * * * * * * * * *
      10 ! * * * * * * ! ! !
   */
    "return a new grid and 'You sank my Destroyer' when a destroyer is completely hit" in {
      val hitPoint = (9, 'A')
      val newShipPlacement = correctShipPlacements - destroyerP + destroyerP.hit((10, 'A'))
      val grid = GridImpl(newShipPlacement)
      val (nextGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      nextGrid should not be grid
      attackResult.getMessage shouldBe "You sunk my Destroyer!"
    }

    "return same grid with hit when you hit same spot in a ship" in {
      val hitPoint = (10, 'A')
      val newShipPlacement = correctShipPlacements - destroyerP + destroyerP.copy(hits = Set((10, 'A')))
      val grid = GridImpl(newShipPlacement)
      val (nextGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      nextGrid shouldBe grid
      attackResult.getMessage shouldBe "You hit my ship!"
    }

    "return same grid with sunk when you hit an already sunk ship" in {
      //TODO: Sent funny message if she sends same spot again
      val hitPoint = (9, 'A')
      val newShipPlacement = correctShipPlacements - destroyerP + destroyerP.copy(hits = Set((10, 'A'), (9, 'A')))
      val grid = GridImpl(newShipPlacement)
      val (nextGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      nextGrid shouldBe grid
      attackResult.getMessage shouldBe "You sunk my Destroyer!"
    }

    /*   A B C D E F G H I J
       1 # # # # # * * * * *
       2 * * * * * * * # # #
       3 * * * * * * * * * *
       4 * * * # * * * * * *
       5 * * * # * * * * * *
       6 * * * # * * * * * *
       7 * * * # * * * * * *
       8 * * * * * * * * * *
       9 # * * * * * * * * *
      10 ! * * * * * * ! ! !
   */
    "return a new grid and 'You beat me' when the last ship is completely hit" in {
      val hitPoint = (9, 'A')
      val newShipPlacement = Set(
        destroyerP.copy(hits = Set((10, 'A'))),
        carrierP.copy(hits = carrierP.getPositionPoints),
        battleShipP.copy(hits = battleShipP.getPositionPoints),
        cruiserP.copy(hits = cruiserP.getPositionPoints),
        submarineP.copy(hits = submarineP.getPositionPoints)
      )
      val grid = GridImpl(newShipPlacement)
      val (nextGrid, attackResult) = grid.attack(hitPoint._1, hitPoint._2)
      nextGrid should not be grid
      attackResult.getMessage shouldBe "You win!"
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
      val _ = GridImpl.ShipPlacement(Ship.carrier, 10, 'F', HORIZONTAL)
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
      val _ = GridImpl.ShipPlacement(Ship.carrier, 1, 'J', VERTICAL)
    }

    "fail to create ShipPlacement, if the shipPlacement column is outside the Grid" in {
      val thrown = intercept[IllegalArgumentException] {
        val _ = carrierP.copy(col = ('A' + GridImpl.size + 1).toChar)
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
        val _ = carrierP.copy(row = GridImpl.size + 1)
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

    "hit a shipPlacement should succeed if hitpoint is correct" in {
      val hitPoint = (9, 'A')
      val newShipPlacement = destroyerP.hit(hitPoint)
      newShipPlacement should not be destroyerP
    }

    "hit a shipPlacement returns exception if hitpoint is not part of the ship" in {
      val wrongHitPoint = (2, 'A')
      val thrown = intercept[IllegalArgumentException] {
        val _ = destroyerP.hit(wrongHitPoint)
      }
      assert(thrown.getMessage === "requirement failed: Invalid hit point(s)")
    }

  }

  "gridAsRowStrings" should {
    "return the grid as a row of strings" in {
      val grid: Grid = GridImpl(correctShipPlacements)
      val expected =
        "C C C C C - - - - -\n" +
        "- - - - - - - c c c\n" +
        "- - - - - - - - - -\n" +
        "- - - B - - - - - -\n" +
        "- - - B - - - - - -\n" +
        "- - - B - - - - - -\n" +
        "- - - B - - - - - -\n" +
        "- - - - - - - - - -\n" +
        "D - - - - - - - - -\n" +
        "D - - - - - - S S S"

      val result = grid.gridAsRowStrings.mkString("\n")

      result shouldBe expected
    }
  }

}
