package scorex.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.SignaturesMessageContentV1

class SignaturesMessageContentV1Serializer extends NullableSerializer[SignaturesMessageContentV1] {
  import scorex.crypto.signatures.Curve25519.SignatureLength

  override def write(kryo: Kryo, output: Output, content: SignaturesMessageContentV1): Unit = {
    output.writeInt(content.signatures.length)
    content.signatures.foreach(s => output.writeBytes(s))
  }

  override def read(kryo: Kryo, input: Input, c: Class[SignaturesMessageContentV1]): SignaturesMessageContentV1 = {
    val lenght = input.readInt()
    val signatures = (1 to lenght).foldLeft(Seq[Array[Byte]]()) { (result, i) =>
      val signature = input.readBytes(SignatureLength)
      result :+ signature
    }

    SignaturesMessageContentV1(signatures)
  }
}
