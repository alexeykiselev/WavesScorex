package scorex.network

import java.net.{InetAddress, InetSocketAddress}

import org.scalacheck.Gen
import org.scalatest.{Matchers, PropSpec}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import scorex.app.{ApplicationVersion, ApplicationVersionV1}
import scorex.serializers.ScorexKryoInstantiator

class BinaryCompatibilitySpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks
  with Matchers {

  val pool = ScorexKryoInstantiator.defaultPool

  val MaxVersion = 999
  val MaxIp = 255
  val MaxPort = 65535

  val appVersionGen = for {
    fd <- Gen.choose(0, MaxVersion)
    sd <- Gen.choose(0, MaxVersion)
    td <- Gen.choose(0, MaxVersion)
  } yield ApplicationVersion(fd, sd, td)

  val isGen = for {
    ip1 <- Gen.choose(0, MaxIp)
    ip2 <- Gen.choose(0, MaxIp)
    ip3 <- Gen.choose(0, MaxIp)
    ip4 <- Gen.choose(0, MaxIp)
    port <- Gen.choose(0, MaxPort)
  } yield new InetSocketAddress(InetAddress.getByName(s"$ip1.$ip2.$ip3.$ip4"), port)

  val validNumbers =
    for (n <- Gen.choose(Integer.MIN_VALUE + 1, Integer.MAX_VALUE)) yield n

  property("handshake and handsake2 should be binary compatible") {
    forAll(Gen.alphaStr suchThat (_.size > 0), appVersionGen, Gen.alphaStr, isGen, Gen.posNum[Long], Gen.posNum[Long]) {
      (appName: String,
       av: ApplicationVersion,
       nodeName: String,
       isa: InetSocketAddress,
       nonce: Long,
       time: Long) =>

        val h1 = Handshake(appName, av, nodeName, nonce, None, time)
        val h1RestoredAsHandshake2 = pool.fromBytes(h1.bytes, classOf[HandshakeV1])
        h1RestoredAsHandshake2.applicationName should be(h1.applicationName)
        h1RestoredAsHandshake2.applicationVersion.firstDigit should be(h1.applicationVersion.firstDigit)
        h1RestoredAsHandshake2.applicationVersion.secondDigit should be(h1.applicationVersion.secondDigit)
        h1RestoredAsHandshake2.applicationVersion.thirdDigit should be(h1.applicationVersion.thirdDigit)
        h1RestoredAsHandshake2.declaredAddress should be(h1.declaredAddress)
        h1RestoredAsHandshake2.nodeNonce should be(h1.nodeNonce)
        h1RestoredAsHandshake2.time should be(h1.time)

        val h2 = Handshake(appName, av, nodeName, nonce, Some(isa), time)
        val h2RestoredAsHandshake2 = pool.fromBytes(h2.bytes, classOf[HandshakeV1])
        h2RestoredAsHandshake2.applicationName should be(h2.applicationName)
        h2RestoredAsHandshake2.applicationVersion.firstDigit should be(h2.applicationVersion.firstDigit)
        h2RestoredAsHandshake2.applicationVersion.secondDigit should be(h2.applicationVersion.secondDigit)
        h2RestoredAsHandshake2.applicationVersion.thirdDigit should be(h2.applicationVersion.thirdDigit)
        h2RestoredAsHandshake2.declaredAddress should be(h2.declaredAddress)
        h2RestoredAsHandshake2.nodeNonce should be(h2.nodeNonce)
        h2RestoredAsHandshake2.time should be(h2.time)

        val h3 = HandshakeV1(appName, ApplicationVersionV1(av.firstDigit, av.secondDigit, av.thirdDigit), nodeName, nonce, None, time)
        val h3RestoredAsHandshake = Handshake.parseBytes(pool.toBytesWithoutClass(h3)).get
        h3RestoredAsHandshake.applicationName should be(h3.applicationName)
        h3RestoredAsHandshake.applicationVersion.firstDigit should be(h3.applicationVersion.firstDigit)
        h3RestoredAsHandshake.applicationVersion.secondDigit should be(h3.applicationVersion.secondDigit)
        h3RestoredAsHandshake.applicationVersion.thirdDigit should be(h3.applicationVersion.thirdDigit)
        h3RestoredAsHandshake.declaredAddress should be(h3.declaredAddress)
        h3RestoredAsHandshake.nodeNonce should be(h3.nodeNonce)
        h3RestoredAsHandshake.time should be(h3.time)

        val h4 = HandshakeV1(appName, ApplicationVersionV1(av.firstDigit, av.secondDigit, av.thirdDigit), nodeName, nonce, Some(isa), time)
        val h4RestoredAsHandshake = Handshake.parseBytes(pool.toBytesWithoutClass(h4)).get
        h4RestoredAsHandshake.applicationName should be(h4.applicationName)
        h4RestoredAsHandshake.applicationVersion.firstDigit should be(h4.applicationVersion.firstDigit)
        h4RestoredAsHandshake.applicationVersion.secondDigit should be(h4.applicationVersion.secondDigit)
        h4RestoredAsHandshake.applicationVersion.thirdDigit should be(h4.applicationVersion.thirdDigit)
        h4RestoredAsHandshake.declaredAddress should be(h4.declaredAddress)
        h4RestoredAsHandshake.nodeNonce should be(h4.nodeNonce)
        h4RestoredAsHandshake.time should be(h4.time)
    }
  }

}
