package scorex.network.messages

import scorex.block.Block

case class BlockMessageContentV1(block: Block) extends MessageContent {
  override val id: Byte = 23
  override val name: String = "BlockMessageV1"
}
