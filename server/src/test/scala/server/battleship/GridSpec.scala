package server.battleship

import org.scalatest.{Matchers, WordSpec}
import Grid.ShipPlacement.Direction._
import server.battleship.Grid.ShipPlacement

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






  "create" should {
    "return a grid with 5 ships positioned" in {

    }

    "should return error if ship number is wrong" in {

    }

    "should return error if ship combination is wrong" in {

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



  "placeShip" should {
    "return true, if all ships are placed correctly" in {

      Grid(correctShipPlacements) shouldBe a [Grid]

    }


    /*    A B C D E F G H I J
        1 # # # # * * * * * *
        2 * * * * * * * * * *
        3 * * * * * * * * * *
        4 * * * * * * * * * *
        5 * * * * * * * * * *
        6 * * * * * * * * * *
        7 * * * * * * * * * *
        8 * * * * * * * * * *
        9 * * * * * * * * * *
       10 * * * * * * * * * *
    */
    "Fails to create Grid if one ship is on top of the other" in {

    }

    /*    A B C D E F G H I J
        1 # # # # * * * * * *
        2 * * * * * * * * * *
        3 * * * * * * * * * *
        4 * * * * * * * * * *
        5 * * * * * * * * * *
        6 * * * * * * * * * *
        7 * * * * * * * * * *
        8 * * * * * * * * * *
        9 * * * * * * * * * *
       10 * * * * * * * * * *
    */
    "Fails to create Grid if group of ships is not correct" in {

    }
  }

  "ShipPlacement" should {
    /*    A B C D E F G H I J
        1 # # # # * * * * * *
        2 * * * * * * * * * *
        3 * * * * * * * * * *
        4 * * * * * * * * * *
        5 * * * * * * * * * *
        6 * * * * * * * * * *
        7 * * * * * * * * * *
        8 * * * * * * * * * *
        9 * * * * * * * * * *
       10 * * * * * * * * * *
    */
    "creates object, if the shipPlacement is inside the Grid" in {
      val _ = Grid.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL, Set())
    }

    "fail to create ShipPlacement, if the shipPlacement column is outside the Grid" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(col = ('A' + Grid.size + 1).toChar)
      }
      assert(thrown.getMessage === "requirement failed: Column out of boundaries")

    }

    "fail to create ShipPlacement, if the shipPlacement column is a special character" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(col = 13.toChar)
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
        7 * * * * * * * * * *
        8 # * * * * * * * * *
        9 # * * * * * * * * *
       10 # * * * * * * * * *
          #
    */
    "fail to create ShipPlacement, if the shipPlacement vertical with ship out of boundaries TEST" in {
      val thrown = intercept[IllegalArgumentException] {
        val wrongPlacement = carrier.copy(row = 8, isHorizontal = false)
      }
      assert(thrown.getMessage === "requirement failed: Row out of boundaries")

    }

  }

}
