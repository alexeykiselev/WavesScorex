package scorex.serializers

import java.net.InetSocketAddress

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.app.ApplicationVersionV1
import scorex.network.HandshakeV1

class HandshakeV1Serializer extends NullableSerializer[HandshakeV1] {

  override def write(kryo: Kryo, output: Output, handshake: HandshakeV1): Unit = {
    kryo.writeObject(output, handshake.applicationName, new ByteLengthUtf8StringSerializer)
    kryo.writeObject(output, handshake.applicationVersion)
    kryo.writeObject(output, handshake.nodeName, new ByteLengthUtf8StringSerializer)
    output.writeLong(handshake.nodeNonce)
    kryo.writeObject(output, handshake.declaredAddress, new InetSocketAddressOptionSerializer())
    output.writeLong(handshake.time)
  }

  override def read(kryo: Kryo, input: Input, c: Class[HandshakeV1]): HandshakeV1 = {
    val applicationName = kryo.readObject(input, classOf[String], new ByteLengthUtf8StringSerializer)
    val applicationVersion = kryo.readObject(input, classOf[ApplicationVersionV1])
    val nodeName = kryo.readObject(input, classOf[String], new ByteLengthUtf8StringSerializer())
    val nonce = input.readLong()
    val address = kryo.readObject(input, classOf[Option[InetSocketAddress]], new InetSocketAddressOptionSerializer())
    val time = input.readLong()

    HandshakeV1(applicationName, applicationVersion, nodeName, nonce, address, time)
  }

}
