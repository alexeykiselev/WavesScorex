package scorex.network

import java.net.InetSocketAddress

import scorex.app.ApplicationVersionV1

case class HandshakeV1(applicationName: String, applicationVersion: ApplicationVersionV1, nodeName: String,
                       nodeNonce: Long, declaredAddress: Option[InetSocketAddress], time: Long) {
  require(Option(applicationName).isDefined)
  require(Option(applicationVersion).isDefined)
}
