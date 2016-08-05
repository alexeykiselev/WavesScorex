package scorex.network.messages

case class GetBlockMessageContentV1(blockId: Array[Byte]) extends MessageContent {
  override val id: Byte = 22
  override val name: String = "GetBlockMessageV1"
}
