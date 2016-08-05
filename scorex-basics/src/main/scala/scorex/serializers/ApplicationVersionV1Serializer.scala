package scorex.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import scorex.app.ApplicationVersionV1

class ApplicationVersionV1Serializer extends NullableSerializer[ApplicationVersionV1] {
  override def write(kryo: Kryo, output: Output, version: ApplicationVersionV1): Unit = {
    output.writeInt(version.firstDigit)
    output.writeInt(version.secondDigit)
    output.writeInt(version.thirdDigit)
  }

  override def read(kryo: Kryo, input: Input, c: Class[ApplicationVersionV1]): ApplicationVersionV1 = {
    val first = input.readInt()
    val second = input.readInt()
    val third = input.readInt()

    ApplicationVersionV1(first, second, third)
  }
}
