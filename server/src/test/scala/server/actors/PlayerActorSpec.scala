package server.actors

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalatest.WordSpecLike
import server.actors.PlayerActorBehavior

class PlayerActorSpec extends TestKit(ActorSystem("PlayerActor")) with WordSpecLike {

  "PlayerActor" should {
    "respond with an attack when receiving GetAttack" in new PlayerActorBehavior {

    }
  }

}
