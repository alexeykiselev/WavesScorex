package scorex.network.messages

import java.net.InetSocketAddress

case class PeersMessageContentV1(peers: Seq[InetSocketAddress]) extends MessageContent {
  override val id: Byte = 2
  override val name: String = "PeersMessageV1"
}
