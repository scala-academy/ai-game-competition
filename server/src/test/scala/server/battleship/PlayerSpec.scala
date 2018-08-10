package server.battleship

import java.io.{ByteArrayInputStream, OutputStream}

import org.scalatest.{Matchers, WordSpec}
import server.battleship.GridImpl.ShipPlacement.Direction.{HORIZONTAL, VERTICAL}


class PlayerSpec extends WordSpec with Matchers {

  val nullOut = new OutputStream {
    override def write(b: Int): Unit = ()
  }

  val stringOut = new OutputStream {
    val stringBuilder = new StringBuilder
    override def write(i: Int): Unit = stringBuilder.append(i.toChar)
  }

  def mockStdIn(input: String, body: => Unit): Unit = {
    val in = new ByteArrayInputStream(input.getBytes)
    Console.withIn(in) {
      Console.withOut(nullOut) {
        body
      }
    }
  }

  def mockStdOut(body: => Unit): String = {
    Console.withOut(stringOut) {
      body
    }
    stringOut.stringBuilder.mkString
  }

  "A Human interface player" should {

    "Pick ship placements" in {

      val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)
      val battleShipP = GridImpl.ShipPlacement(Ship.battleShip, 4, 'D', VERTICAL)
      val cruiserP = GridImpl.ShipPlacement(Ship.cruiser, 2, 'H', HORIZONTAL)
      val submarineP = GridImpl.ShipPlacement(Ship.submarine, 10, 'H', HORIZONTAL)
      val destroyerP = GridImpl.ShipPlacement(Ship.destroyer, 9, 'A', VERTICAL)

      val expected = Set(carrierP, battleShipP, cruiserP, submarineP, destroyerP)


      val input = "1 A H\n4 D V\n2 H H\n10 H H\n9 A V"

      mockStdIn(input, {
        val shipPlacements = HumanInterface.addShips
        shipPlacements shouldBe expected
      })

    }

    "Pick ship placements with incorrect input" in {
      val carrierP = GridImpl.ShipPlacement(Ship.carrier, 1, 'A', HORIZONTAL)

      val expected = Set(carrierP)

      val input = "111 A H\n1 A H"

      mockStdIn(input, {
        val shipPlacements = HumanInterface.getShipPlacements(Seq(Ship.carrier))
        shipPlacements shouldBe expected
      })

    }

    "display the players attacks in a grid" in {

      val humanInterface = new HumanInterface() {

        var attacks = (2, 'B') +: (0 to 4).map(i => (1, ('A' + i).toChar))

        override def getAttack: (Int, Char) = {
          val currentAttack = attacks.head
          attacks = attacks.tail
          currentAttack
        }
      }

      val results = Seq(Miss, Hit, Hit, Hit, Hit, Sunk(Ship.carrier))

      for(r <- results) {
        humanInterface.getAttack
        humanInterface.processAttackResult(r)
      }

      val outputGrid = mockStdOut({
        humanInterface.showAttackGrid()
      })

      outputGrid shouldBe """X X X X C · · · · ·
                            |· ~ · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |· · · · · · · · · ·
                            |""".stripMargin

    }

  }

  "A player" should {
    "Provide attack locations" in {

      val input = "1 A"
      mockStdIn(input, {
        new HumanInterface().getAttack shouldBe(1, 'A')
      })

    }
  }

  "A RandomAttackAI" should {
    "Make random attacks" in {
      def getAttackSequence = (1 to 10).map(_ => RandomAttackAI.getAttack)

      val numberOfSequences = 5
      //Compare different sequences of attacks should be different
      val attackSequences: Set[Seq[(Int, Char)]] = (1 to numberOfSequences).map { _ => getAttackSequence }.toSet

      attackSequences.size shouldBe numberOfSequences
    }
  }

}
