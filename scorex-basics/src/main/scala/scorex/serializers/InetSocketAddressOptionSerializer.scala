package scorex.serializers

import java.net.{InetAddress, InetSocketAddress}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}

class InetSocketAddressOptionSerializer extends NullableSerializer[Option[InetSocketAddress]] {

  private val PortValueLength: Int = 4

  override def write(kryo: Kryo, output: Output, optionalAddress: Option[InetSocketAddress]): Unit = {
    if (optionalAddress.isDefined) {
      val address = optionalAddress.get
      val addressBytes = address.getAddress.getAddress
      output.writeInt(addressBytes.length + PortValueLength)
      output.writeBytes(addressBytes)
      output.writeInt(address.getPort)
    }
    else output.writeInt(0)
  }

  override def read(kryo: Kryo, input: Input, `type`: Class[Option[InetSocketAddress]]): Option[InetSocketAddress] = {
    val length = input.readInt()
    if (length == 0) None
    else {
      val bytes = input.readBytes(length - PortValueLength)
      val port = input.readInt()
      val address = new InetSocketAddress(InetAddress.getByAddress(bytes), port)

      Some(address)
    }
  }
}
