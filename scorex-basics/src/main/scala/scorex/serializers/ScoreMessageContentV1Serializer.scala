package scorex.serializers
import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.ScoreMessageContentV1

class ScoreMessageContentV1Serializer extends NullableSerializer[ScoreMessageContentV1] {

  override def write(kryo: Kryo, output: Output, content: ScoreMessageContentV1): Unit = {
    output.writeBytes(content.score.toByteArray)
  }

  override def read(kryo: Kryo, input: Input, c: Class[ScoreMessageContentV1]): ScoreMessageContentV1 = {
    val bytes = input.readBytes(input.available())

    ScoreMessageContentV1(BigInt(1, bytes))
  }

}
