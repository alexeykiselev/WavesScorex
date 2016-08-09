package scorex.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.GetSignaturesMessageContentV1

class GetSignaturesMessageContentV1Serializer extends NullableSerializer[GetSignaturesMessageContentV1] {

  import scorex.crypto.signatures.Curve25519.SignatureLength

  override def write(kryo: Kryo, output: Output, content: GetSignaturesMessageContentV1): Unit = {
    output.writeInt(content.blockIds.length)
    content.blockIds.foreach(idBytes => output.write(idBytes))
  }

  override def read(kryo: Kryo, input: Input, c: Class[GetSignaturesMessageContentV1]): GetSignaturesMessageContentV1 = {
    val length = input.readInt()
    val ids: Seq[Array[Byte]] = (1 to length).foldLeft(Seq[Array[Byte]]()) { (result, i) =>
      val id = input.readBytes(SignatureLength)
      result :+ id
    }

    GetSignaturesMessageContentV1(ids)
  }
}
