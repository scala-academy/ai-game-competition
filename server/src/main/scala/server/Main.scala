package server

import com.twitter.util.Await

object Main extends App {
  val defaultPort = 8080

  Await.ready(Server(defaultPort).server)

}
