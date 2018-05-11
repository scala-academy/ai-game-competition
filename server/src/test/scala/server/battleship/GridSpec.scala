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

    /*  A B C D E F G H I J
      1 # # # # # * * * * *
      2 * * * * * * * # # #
      3 * * * * * * * * * *
      4 * * * # * * * * * *
      5 * * * # * * * * * *
      6 * * * # * * * * * *
      7 * * * # * * * * * *
      8 * * * * * * * * * *
      9 # * * * * * * * * *
     10 # * * * * * * * # # #
  */
    "return false, if ship is out of boundaries" in {

      val outOfBoundaries = correctShipPlacements.map(s => if (s.ship.name == "submarine") s.copy(col = 'I') else s)
      val thrown = intercept[Exception] {
        Grid(outOfBoundaries)
      }
      assert(thrown.getMessage === "Boom!")


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
    "return false if one ship is on top of the other" in {

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
    "return false if group of ships is not correct" in {

    }
  }


}
