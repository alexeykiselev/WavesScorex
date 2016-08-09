package scorex.serializers

import java.net.InetSocketAddress

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.network.messages.PeersMessageContentV1

class PeersMessageContentV1Serializer extends NullableSerializer[PeersMessageContentV1]{
  override def write(kryo: Kryo, output: Output, content: PeersMessageContentV1): Unit = {
    output.writeInt(content.peers.length)
    content.peers.foreach(p => kryo.writeObject(output, p, new InetSocketAddressWithoutLengthSerializer()))
  }

  override def read(kryo: Kryo, input: Input, c: Class[PeersMessageContentV1]): PeersMessageContentV1 = {
    val peersLength = input.readInt()
    val peers: Seq[InetSocketAddress] = (1 to peersLength).foldLeft(Seq[InetSocketAddress]()) { (result, i) =>
      val peer = kryo.readObject(input, classOf[InetSocketAddress], new InetSocketAddressWithoutLengthSerializer())
      result :+ peer
    }

    PeersMessageContentV1(peers)
  }
}
