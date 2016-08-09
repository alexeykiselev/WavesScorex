package scorex.serializers

import java.net.InetSocketAddress

import com.esotericsoftware.kryo.Kryo
import com.twitter.chill.IKryoRegistrar
import scorex.app.ApplicationVersionV1
import scorex.network.HandshakeV1
import scorex.network.messages._

class ScorexRegistrar extends IKryoRegistrar {
  override def apply(k: Kryo): Unit = {
    k.setRegistrationRequired(true)
    k.setReferences(false)
    k.register(classOf[ApplicationVersionV1], new ApplicationVersionV1Serializer)
    k.register(classOf[String], new ByteLengthUtf8StringSerializer)
    k.register(classOf[HandshakeV1], new HandshakeV1Serializer)
    k.register(classOf[InetSocketAddress], new InetSocketAddressSerializer)
    k.register(classOf[Option[InetSocketAddress]], new InetSocketAddressOptionSerializer)
    k.register(classOf[NetworkMessage], new NetworkMessageSerializer)
    k.register(classOf[ScoreMessageContentV1], new ScoreMessageContentV1Serializer)
    k.register(classOf[GetBlockMessageContentV1], new GetBlockMessageContentV1Serializer)
    k.register(classOf[GetPeersMessageContentV1], new GetPeersMessageContentV1Serializer)
    k.register(classOf[PeersMessageContentV1], new PeersMessageContentV1Serializer)
    k.register(classOf[GetSignaturesMessageContentV1], new GetSignaturesMessageContentV1Serializer)
    k.register(classOf[SignaturesMessageContentV1], new SignaturesMessageContentV1Serializer)
  }
}

