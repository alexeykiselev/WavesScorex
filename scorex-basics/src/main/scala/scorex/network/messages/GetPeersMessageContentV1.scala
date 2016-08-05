package scorex.network.messages

case class GetPeersMessageContentV1() extends MessageContent {
  override val id: Byte = 1
  override val name: String = "GetPeersMessageV1"
}
