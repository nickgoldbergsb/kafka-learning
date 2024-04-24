package resources

import cats.effect.IO
import cats.effect.kernel.Resource
import sttp.capabilities
import sttp.capabilities.fs2.Fs2Streams
import sttp.client3.SttpBackend
import sttp.client3.httpclient.fs2.HttpClientFs2Backend

object Resource {
  val resourceBuilder: Resource[IO, SttpBackend[IO, Fs2Streams[IO] with capabilities.WebSockets]] =
    for {
      backend <- HttpClientFs2Backend.resource[IO]()
    } yield backend
}
