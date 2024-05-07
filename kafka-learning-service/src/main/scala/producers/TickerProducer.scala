package producers

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

import java.util.Properties
import java.util.concurrent.Future

object TickerProducer {

  private val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

  private val topic = "ticker"

  val kafkaProducer = new KafkaProducer[String, String](props)

  def toKafkaBroker(event: String): Future[RecordMetadata] = {
    val producerRecord = new ProducerRecord[String, String](topic, event)
    kafkaProducer.send(producerRecord)
  }
}
