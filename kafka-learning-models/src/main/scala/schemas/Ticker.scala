package schemas

import io.circe._
import io.circe.generic.semiauto._

case class Ticker(`type`: String,
                  sequence: String,
                  product_id: String,
                  price: String,
                  open_24h: String,
                  volume_24h: String,
                  low_24h: String,
                  high_24h: String,
                  volume_30d: String,
                  best_bid: String,
                  best_bid_size: String,
                  best_ask: String,
                  best_ask_size: String,
                  side: String,
                  time: String,
                  trade_id: String,
                  last_size: String)

object Ticker {
  implicit val tickerCodec: Encoder[Ticker] = deriveCodec[Ticker]
}