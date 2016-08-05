package scorex.serializers

import com.esotericsoftware.kryo.io.{Input, Output}
import com.esotericsoftware.kryo.{Kryo, Serializer}

class ByteLengthUtf8StringSerializer extends NullableSerializer[String] {

  private val DefaultCharset: String = "UTF-8"

  override def write(kryo: Kryo, output: Output, string: String): Unit = {
    output.writeByte(string.length)
    output.writeBytes(string.getBytes(DefaultCharset))
  }

  override def read(kryo: Kryo, input: Input, c: Class[String]): String = {
    val length = input.readByte()
    val bytes = input.readBytes(length)

    new String(bytes, DefaultCharset)
  }

}
