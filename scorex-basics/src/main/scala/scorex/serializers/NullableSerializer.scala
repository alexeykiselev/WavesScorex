package scorex.serializers

import com.esotericsoftware.kryo.Serializer

abstract class NullableSerializer[T] extends Serializer[T] {
  override def getAcceptsNull: Boolean = true
}
