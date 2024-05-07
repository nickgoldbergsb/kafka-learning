package schemas

import io.circe._
import io.circe.generic.semiauto._

case class Request(`type`: String,
                   product_ids: Seq[String],
                   channels: Seq[String])

object Request {
  implicit val requestCodec: Encoder[Request] = deriveCodec[Request]
}