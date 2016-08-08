package scorex.network.messages

object NetworkMessagesRegistry {

  private var registry: Map[Byte, Class[_ <: MessageContent]] = Map()

  def register(messageContent: MessageContent): Unit = {
    registry += (messageContent.id -> messageContent.getClass)
  }

  def find(id: Byte): Class[_ <: MessageContent] = {
    registry.get(id) match {
      case Some(value) => value
      case None => throw new NetworkMessageException(s"Message content for message id = $id was not found")
    }
  }

}
