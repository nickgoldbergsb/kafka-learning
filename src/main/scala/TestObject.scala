import cats.effect.IO
import cats.effect.unsafe.IORuntime
import fs2.{Pipe, Stream}
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3._
import sttp.client3.httpclient.fs2.HttpClientFs2Backend
import sttp.ws.WebSocketFrame
import io.circe._
import io.circe.syntax.EncoderOps

object TestObject extends App {
  implicit val runtime: IORuntime = cats.effect.unsafe.implicits.global

  case class Request(`type`: String,
                     product_ids: Seq[String],
                     channels: Seq[String])

  implicit val requestEncoder: Encoder[Request] = request => Json.obj(
    "type" -> request.`type`.asJson,
    "product_ids" -> request.product_ids.asJson,
    "channels" -> request.channels.asJson
  )

  val request = Request("subscribe", Seq("ETH-USD"), Seq("ticker"))

  val requestJson = request.asJson.toString()

  def webSocketFramePipe: Pipe[IO, WebSocketFrame.Data[_], WebSocketFrame] = { input =>
    Stream.emit(WebSocketFrame.text(requestJson)) ++ input.flatMap {
      case WebSocketFrame.Text(a, _, _) =>
        println(a)
        Stream.empty
      case _ => Stream.empty // ignoring
    }
  }

  HttpClientFs2Backend
    .resource[IO]()
    .use { backend =>
      basicRequest
        .response(asWebSocketStream(Fs2Streams[IO])(webSocketFramePipe))
        .get(uri"wss://ws-feed.exchange.coinbase.com")
        .send(backend)
        .void
    }
    .unsafeRunSync()
}
