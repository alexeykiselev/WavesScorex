package scorex.network.messages

case class SignaturesMessageContentV1(signatures: Seq[Array[Byte]]) extends MessageContent {
  override val id: Byte = 21
  override val name: String = "SignaturesMessageV1"
}
