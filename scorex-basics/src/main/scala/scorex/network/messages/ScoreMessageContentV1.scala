package scorex.network.messages

case class ScoreMessageContentV1(score: BigInt) extends MessageContent {
  override val id: Byte = 24
  override val name: String = "ScoreMessageV1"
}
