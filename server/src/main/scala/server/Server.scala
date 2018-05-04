package server

import com.twitter.finagle.Http
import com.twitter.util._
import com.typesafe.scalalogging.LazyLogging
import io.finch.circe._
import io.finch.{Endpoint, Text, _}

object Server {

  def apply(port: Int): Server = new Server(port)
}

class Server private[server](port: Int) extends LazyLogging {

  val api: Endpoint[String] = get("hello") { Ok("Hello, world!") }

  val server = Http.server.serve(s":$port", api.toServiceAs[Text.Plain])
  logger.debug(s"Starting server at $port")

  def terminate: Future[Unit] = {
    server.close()
  }
}