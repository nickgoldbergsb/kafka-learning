import cats.effect.{ExitCode, IO, IOApp}
import fs2.{Pipe, Stream}
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3._
import sttp.ws.WebSocketFrame
import io.circe.syntax.EncoderOps
import schemas.Request
import resources.Resource
import producers.TickerProducer.toKafkaBroker


object Runner extends IOApp {

  val request = Request("subscribe", Seq("ETH-USD"), Seq("ticker"))

  val requestJson = request.asJson.toString()

  private def webSocketFramePipe: Pipe[IO, WebSocketFrame.Data[_], WebSocketFrame] = input =>
    Stream.emit(WebSocketFrame.text(requestJson)) ++ input.flatMap {
      case WebSocketFrame.Text(event, _, _) =>
        toKafkaBroker(event)
        Stream.empty
      case _ => Stream.empty
    }

  override def run(args: List[String]): IO[ExitCode] = Resource
    .resourceBuilder
    .use { backend =>
      basicRequest
        .response(asWebSocketStream(Fs2Streams[IO])(webSocketFramePipe))
        .get(uri"wss://ws-feed.exchange.coinbase.com")
        .send(backend)
        .void
        .as(ExitCode.Success)
    }
}
