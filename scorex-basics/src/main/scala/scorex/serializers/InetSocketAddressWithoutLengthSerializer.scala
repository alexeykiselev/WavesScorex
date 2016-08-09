package scorex.serializers

import java.net.{InetAddress, InetSocketAddress}

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}

class InetSocketAddressWithoutLengthSerializer extends NullableSerializer[InetSocketAddress] {

  private val AddressValueLength: Int = 4

  override def write(kryo: Kryo, output: Output, address: InetSocketAddress): Unit = {
    val addressBytes = address.getAddress.getAddress
    output.writeBytes(addressBytes)
    output.writeInt(address.getPort)
  }

  override def read(kryo: Kryo, input: Input, c: Class[InetSocketAddress]): InetSocketAddress = {
    val bytes = input.readBytes(AddressValueLength)
    val port = input.readInt()

    new InetSocketAddress(InetAddress.getByAddress(bytes), port)
  }

}
