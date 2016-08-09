package scorex.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.{NetworkMessage, NetworkMessageException}

class NetworkMessageSerializer extends NullableSerializer[NetworkMessage] {
  override def write(kryo: Kryo, output: Output, message: NetworkMessage): Unit = {
    output.writeBytes(NetworkMessage.MagicBytes)
    output.writeByte(message.contentId)
    output.writeInt(message.content.length)
    output.writeBytes(message.checksum)
    output.writeBytes(message.content)
  }

  override def read(kryo: Kryo, input: Input, c: Class[NetworkMessage]): NetworkMessage = {
    val magic: Array[Byte] = input.readBytes(NetworkMessage.MagicBytesLength)
    if (!magic.sameElements(NetworkMessage.MagicBytes))
      throw new NetworkMessageException(s"Incorrect magic bytes")

    val contentId = input.readByte()
    val length = input.readInt()
    val checksum = if (length != 0) input.readBytes(NetworkMessage.ChecksumLength) else Array[Byte]()
    val content = input.readBytes(length)

    if (!checksum.isEmpty && !NetworkMessage.verifyChecksum(content, checksum))
      throw new NetworkMessageException(s"Incorrect message checksum")

    NetworkMessage(contentId, content)
  }
}
