package http4sRequest

import cats.effect.unsafe.implicits.global
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.implicits.http4sLiteralsSyntax


object apiCall extends IOApp {
  def callEffect(client: Client[IO]): IO[String] =
    client.expect[String](uri"https://api.chess.com/pub/player/nickgoldbergg/games/2024/03")

  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO].resource
      .use { client =>
        println(callEffect(client).unsafeRunSync())
        IO.unit
      }
      .as(ExitCode.Success)
}
