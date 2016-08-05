package scorex.network.messages

import scorex.crypto.hash.FastCryptographicHash.hash

case class NetworkMessage(contentId: Byte, content: Array[Byte]) {

  val checksum: Array[Byte] = NetworkMessage.calculateChecksum(content)

}

object NetworkMessage {
  val MagicBytesLength = 4
  val MagicBytes: Array[Byte] = Array(0x12: Byte, 0x34: Byte, 0x56: Byte, 0x78: Byte)
  val ChecksumLength: Int = 4

  def calculateChecksum(data: Array[Byte]): Array[Byte] = {
    hash(data).take(ChecksumLength)
  }

  def verifyChecksum(data: Array[Byte], checksum: Array[Byte]): Boolean = {
    checksum.sameElements(calculateChecksum(data))
  }

}
