package server

import com.twitter.finagle.http.{RequestBuilder, Response}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.io.Buf
import com.twitter.util.{Await, Future}
import com.typesafe.scalalogging.StrictLogging
import io.circe.Json
import org.scalatest._

class IntegrationTest extends FunSuite with BeforeAndAfterAll with Matchers with StrictLogging {

  private val port = 8080
  private val serverUrl: String = s"localhost:$port"
  private val gamesUrl: String = s"http://$serverUrl/games"

  var server: Server = _

  override def beforeAll(): Unit = {
    logger.debug(s"Server starting from IntegrationTest")
    server = Server(port)
  }

  override def afterAll(): Unit = {
    logger.debug(s"Shutting down servers from IntegrationTest")
    Await.result(server.terminate)
  }

  test("return a hello message on a get to /hello") {
    val expected = "Hello, world!"

    val response: Future[http.Response] = sendGet("/hello")
    val r = Await.result(response)
    val result = r.contentString

    result shouldBe expected
  }

  lazy val client: Service[http.Request, http.Response] = Http.newService(serverUrl)

  def sendGet(uri: String): Future[Response] = {
    val request = http.Request(http.Method.Get, uri)
    client(request)
  }

  def sendPost(payload: Json, url: String): Future[Response] = {
    val bytes = Buf.ByteArray(payload.toString().getBytes(): _*)
    val request = RequestBuilder().url(url).buildPost(bytes)
    client(request)
  }
}
