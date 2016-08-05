package scorex.network.messages

case class GetSignaturesMessageContentV1(blockIds: Seq[Array[Byte]]) extends MessageContent {
  override val id: Byte = 20
  override val name: String = "GetSignaturesMessageV1"
}
