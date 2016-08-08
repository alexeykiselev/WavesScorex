package scorex.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.GetBlockMessageContentV1

class GetBlockMessageContentV1Serializer extends NullableSerializer[GetBlockMessageContentV1] {
  override def write(kryo: Kryo, output: Output, content: GetBlockMessageContentV1): Unit = {
    output.writeBytes(content.blockId)
  }

  override def read(kryo: Kryo, input: Input, c: Class[GetBlockMessageContentV1]): GetBlockMessageContentV1 = {
    val blockId = input.readBytes(input.available())

    GetBlockMessageContentV1(blockId)
  }
}
