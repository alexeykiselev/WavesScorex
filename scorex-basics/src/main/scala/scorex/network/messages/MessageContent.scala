package scorex.network.messages

abstract class MessageContent {

  val id: Byte
  val name: String

  override def toString: String = s"[$id] '$name'"

}
