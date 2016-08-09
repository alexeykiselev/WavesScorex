package scorex.lagonaki.unit

import java.net.{InetAddress, InetSocketAddress}
import java.nio.ByteBuffer

import akka.actor.{ActorRef, DeadLetter}
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.encode.Base58
import scorex.lagonaki.TestingCommons
import scorex.network.ConnectedPeer
import scorex.network.message.{BasicMessagesRepo, Message, MessageHandler}
import scorex.transaction.History

import scala.util.Try

class MessagesDumpSpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks with Matchers with TestingCommons {
  implicit val consensusModule = application.consensusModule
  implicit val transactionModule = application.transactionModule

  val repo = new BasicMessagesRepo()
  val handler = MessageHandler(repo.specs)

  property("Dump ScoreMessage") {
    forAll(Arbitrary.arbBigInt.arbitrary suchThat (_ > 0)) { n =>
      val message = Message(repo.ScoreMessageSpec, Right(n), None)
      val bytes = message.bytes
      val bytesString = Base58.encode(bytes)

      println(s"$n: $bytesString")
    }
  }

  var ArrayGenerator = Gen.containerOf[Array, Byte](Arbitrary.arbByte.arbitrary)

  property("Dump GetBlockMessage") {
    forAll(ArrayGenerator) { a =>
      val message = Message(repo.GetBlockSpec, Right(a), None)
      val bytes = message.bytes
      val bytesString = Base58.encode(bytes)
      val aString = Base58.encode(a)

      println(s"('$bytesString', '$aString')")
    }
  }

  property("Dump GetPeersMessage") {
    val message = Message(repo.GetPeersSpec, Right(), None)
    val bytes = message.bytes
    val bytesString = Base58.encode(bytes)

    println(s"$bytesString")
  }

  val MaxIp = 255
  val MaxPort = 65535

  val InetSocketAddressGenerator = for {
    ip1 <- Gen.choose(0, MaxIp)
    ip2 <- Gen.choose(0, MaxIp)
    ip3 <- Gen.choose(0, MaxIp)
    ip4 <- Gen.choose(0, MaxIp)
    port <- Gen.choose(0, MaxPort)
  } yield new InetSocketAddress(InetAddress.getByName(s"$ip1.$ip2.$ip3.$ip4"), port)

  val PeersGenerator = Gen.containerOf[Seq, InetSocketAddress](InetSocketAddressGenerator)

  property("Dump PeersMessage") {
    forAll(PeersGenerator) { peers =>
      val message = Message(repo.PeersSpec, Right(peers), None)
      val bytes = message.bytes
      val bytesString = Base58.encode(bytes)

      println(s"('$bytesString', '$peers'),")
    }
  }

  val newScoreMessages = Table(("message", "score"),
    ("7hApEakrcNeNEeYHeGQ", BigInt("1")),
    ("pw6uPnZsmYvAzCJ9DPTzX5mkm8DdrE52VZ9SRWf", BigInt("664613997892457936451903530140172288")),
    ("7hApEakrcNeNSuCs21Y", BigInt("3")),
    ("27vjCghrX1U2uj8EYt8Z1NquKwqfL", BigInt("9223372036854775807")),
    ("3ThqvhQhN7YBYNSacSjWq5jAcDteBQXnjqEXZ", BigInt("2271629875608987087482092144033792")),
    ("5wa9GaFKZT4KjRsEn78TgUDe4NqZod", BigInt("295147905179352825856")),
    ("2fFrwoqfhbvS9HmdYGakR1czZsykSXRoM", BigInt("67699845898419233783545856")),
    ("5wa9GaFKZT4Koyt6t7pPjzpxt1pC5M", BigInt("9223372036854775808")),
    ("pw6uPnZsmYvCwnPhLrXgbzhPTsC5fuuXnqv8ZkT", BigInt("1827688494204259325242734707885473792")),
    ("27vjCghrX1U2vWH2gu19mxVqcuqR1", BigInt("720575940379279360")),
    ("2fFrwoqfhbvS9JyWrSdwQW4sjquGkUS1u", BigInt("116056878683004400771792896")),
    ("4LJDsqz85fmgEqSHcGn7DPLgfH", BigInt("1717986918400")),
    ("FiB1qRD8Fc8CUP8WAbQDM7ud1ZZ", BigInt("2533274790395904")),
    ("4eZo4Fa875dt1owbaE5RJFQiBhf9ko34Xk4aSnPsV", BigInt("2381976568446569244243622252022377480192")),
    ("7hApEakrcNeNNvrHvFK", BigInt("34")),
    ("NosHVjkCvE1gNneGAuNdbSAe5B7t5V1", BigInt("54307214553000919957504")),
    ("7hApEakrcNeNEZi2eUy", BigInt("2")),
    ("7hApEakrcNeNFi9QehC", BigInt("7")),
    ("5wa9GaFKZT4Kaq2bjbem5pnsV4sfvf", BigInt("110680464442257309696")),
    ("FiB1qRD8Fc8DR8zyCNdUzizmfyh", BigInt("501377302265856")),
    ("27vjCghrX1U2r3Ufkzxxaq9yPEkaK", BigInt("576460752303423488")),
    ("ZLDu3MmwdNZjdGCRbzscsuaVCMTHQFvnSRu", BigInt("2614529362970723140586950361088")),
    ("FiB1qRD8Fc8Cn2eNNNvAzKdc9AX", BigInt("642114790621184")),
    ("NosHVjkCvE1gmsgXiaHE54PAtyDQ7Vy", BigInt("340010386766614455386112")),
    ("ZLDu3MmwdNZinnWX6P3DvEBHqFnHyUYyUf9", BigInt("153504564871387154087491403776")),
    ("7hApEakrcNeNE4PYTpB", BigInt("48")),
    ("ZLDu3MmwdNZjRHuKJScDGzKaEG4jFBrjDks", BigInt("102749023260686562816627310592")),
    ("5wa9GaFKZT4KmhSeTjUYrtiexbdnpf", BigInt("12105675798371893248")),
    ("BrsGx1MbABrenBVMGnmnKuowkrWg47kRaLP86b", BigInt("62307562302417931542365955950641152")),
    ("BrsGx1MbABreCpahGQucw1asPsUqzmBcywfhBD", BigInt("107740159814597673292007798831316992")),
    ("ko3Tvh8VhW9nugNPc61PWExF", BigInt("2147483648")),
    ("7hApEakrcNeNFXEm1JR", BigInt("98")),
    ("7hApEakrcNeNVKJPH8r", BigInt("71")),
    ("7hApEakrcNeNYVABLyh", BigInt("100")),
    ("27vjCghrX1U2u4BvR81aBBNmP9Bvw", BigInt("387309567953862656")),
    ("NosHVjkCvE1hYzbHVqVV6AaGrgi7XYb", BigInt("4132070672510939561984")),
    ("4LJDsqz85fmg6TEEvzrVTFXfnf", BigInt("65970697666560")),
    ("8KrbSujopD8rYmy2UJ7voVCo3XWmfmXSyV", BigInt("38376141217846788521872850944")),
    ("8KrbSujopD8rwKDJD9kmvyeM6onZmcPcsZ", BigInt("386856262276681335905976320")),
    ("NosHVjkCvE1gZk5WMZgirjpsJ8P7Ddy", BigInt("4353431601395454181376")),
    ("4eZo4Fa875dt6meQWr9BeS3NB2JdYyxi6FyCAqj67", BigInt("284454791097971996801414710899993739264")),
    ("4eZo4Fa875dsyXGTiv5bk66fnvET1xzSWFVTxNN5m", BigInt("2084229497390748088713169470519580295168")),
    ("7hApEakrcNeNZVUKWub", BigInt("126")),
    ("7hApEakrcNeNKqePkCY", BigInt("105")),
    ("4eZo4Fa875dt9Hh9zdbk1ZqzT1PktVckN9QfH1UHM", BigInt("755001501605832215809362410239235719168")),
    ("3ThqvhQhN7YBWpJb4c4yd7Gt1DsbQkENqZ2zK", BigInt("2555583610060110473417353662038016")),
    ("BrsGx1MbABrepdMAQZqTtC99CDiVEJnn7gvgi3", BigInt("5070602400912917605986812821504000")),
    ("WYJKUwzZy8Xz8Wc3e4CF", BigInt("128")),
    ("FiB1qRD8Fc8DHtQfZ15FQmsiJNK", BigInt("3553621580972032")),
    ("7hApEakrcNeNUiErT6d", BigInt("70")),
    ("pw6uPnZsmYvAjZbEcW6CXoguBoD2eVwzhwsNm67", BigInt("30904550901999294045013514151518011392")),
    ("4LJDsqz85fmg42yV86bQUaees1", BigInt("36558761623552")),
    ("7hApEakrcNeNKkicuNs", BigInt("44")),
    ("NosHVjkCvE1gesPDyWVor8KfwZFYMXm", BigInt("491126114218443102224384")),
    ("ZLDu3MmwdNZhnca9FKhDcThnUXdmitdSTPM", BigInt("7605903601369376408980219232256")),
    ("ZLDu3MmwdNZitZM3STcJcxA3NzYUBtJRRz7", BigInt("435754893828453856764491726848")),
    ("5wa9GaFKZT4Ksk73DbabCrzXrCrxZD", BigInt("802433367206365495296")),
    ("7hApEakrcNeNAsXMXYn", BigInt("107")),
    ("ZLDu3MmwdNZhmwCrwyQxEN1u7NmKpkrtAh5", BigInt("886365068128332276827772944384")),
    ("NosHVjkCvE1hJ3evFtpoKdWDv2oM76f", BigInt("453347182355485940514816")),
    ("ZLDu3MmwdNZi2j3RK8Ft8mvwZCAxQ4squXd", BigInt("8477413389026284122509202685952")),
    ("8KrbSujopD8rkdKczbFN4ZX9HtQ6AtNUZV", BigInt("6808670216069591511945183232")),
    ("2fFrwoqfhbvSAGUT9CN3cW6eBH5UeRwBu", BigInt("74348937906299694244429824")),
    ("7hApEakrcNeNZ1bc6iV", BigInt("50")),
    ("2fFrwoqfhbvSDXjPC22PtR1RcRddkm3p3", BigInt("49868190059103453456629760")),
    ("ZLDu3MmwdNZhZkFgMoWjJC4mtREi9fJMM4K", BigInt("6219410757369750501093200101376")),
    ("8KrbSujopD8rWPGwh9Jr1RvwJYrhPRgv47", BigInt("10754604091291741138186141696")),
    ("BrsGx1MbABreKNWZFHNA1W1HDsUjEFv4iFbQtf", BigInt("12656223592678642344543084802473984")))

  property("scoreMessage should deserialize from new code") {
    forAll(newScoreMessages) { (message, score) =>
      val messageBytes = Base58.decode(message).get
      val restored = handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get
      assert(restored.spec == repo.ScoreMessageSpec)
      restored.data.get match {
        case restoredScore: History.BlockchainScore =>
          assert(score == restoredScore)
        case _ =>
          fail("wrong data type restored")
      }
    }
  }

  val newGetBlockMessages = Table(("message", "id"),
    ("8m89eZRfFLUQS2vUAFSYYTqc53vafgMwZATUJqS9ATZJejQ3EzBuFPBU1fAheMLMaBipVzWEfFA4XqJYhNEw545CAEznyX1Q9fWuDFqep",
      "37HPTJGMD4Be3XQ3GqhDrhski31ASCBuoY6gu6muacLXRazHykTh19RdQto23SYDGW8kWxppJpuXb2xGHRbnGQdv"),
    ("8m89eZRfFLUQFTuubU6V9kpBMVHHhziiFw1Hoa9ak77ndUeNH9CQS4UNzU1mYvpAJiEZX4qFdzC2VXHLwg3WWaUJxJFcHZ6CvXU5bmRsE",
      "67KoacA4Y3Ua9iQ3JVCadBuoAWu8pLQT4gLW3xx7SbP472N2mDtkJBPyng3NfJUEVUe6DYA5Y9uBVaxAcZMvZ8Cx"),
    ("8m89eZRfFLUQY88r5vndASHUfcRpUdZzd2mHvafQbXNfBjJ6FPHbzU21B4mNTLwz17auxjhLbJo2CpzGiSu1uJgitNUeUSFTeuevRShto",
      "4nbSvCDBEYuNvTX8ioTcmQAQ6SAYuNGwaour5M5DSw1tFChVoikjEz4zeuibmA9CQRxgDazELanam5JzY1QMyCMq"),
    ("8m89eZRfFLUQJUmLppXDmPFkt3kgM31ASHmzeRkviDx6XRpPqbHKySo4ixWojDGbz49xLW2LjnjaEmXwjNLnDHCneGeUNbR5Jshrb1RQb",
      "yntui6cnLTQLUXceT7CNaShPuLPaDNm9Jeo2MEMeDx7fSKo3wXQ2xzDatRZJrinVqQDeq53uL8wbjTftFpvq7wu"),
    ("8m89eZRfFLUQ9srkC7y3dgCwVjf1rSSwyfQ2qXEtkhza4YYorhPv5psY7ZT7c7JJ9iKHyscsvQAzqmnEAP8j6kPf6az3fk7B2J8hQbtaK",
      "67rpucUjvLLNQ2TDfM31SD13TaMiYt4U7i5jySYPGDcbeBjhh4HweHrXeRLmG8YQk16Gt1HDXyzy71m95xub6wNw"),
    ("8m89eZRfFLUQV1gVUMNJkdm3afVVyMSgowCevJCzp3EnSRJsKDVwAJZFhysFCWiJFUak8AQAkpG7awVdczGbimnFPBHW97FLKtJp9dZFM",
      "34RfGJ7gHZAAsQTwXVNc6g87ovnCiLUfxS1BPx9sFftk6zTEJqwMTeF5qPRZRqzPXm1HZdy81oBXEGLXAsdazSJj"),
    ("8m89eZRfFLUQGJrLearHvifAM9DgCe1Z6GAa7RWDJvnicG6qTFyXvt5ECt6EB7oSz1TWLNi3pBY4XzBYNpixzVwGtHhiywQV8veLQFBNN",
      "FVTFTUetWizdxmwCoiQPV96YqFBmRXPgsa8JYaPNurh3Y8tYcQM5mEhJ94G7E4RS4enhA3qo37AJvtJR5RNU15G"),
    ("8m89eZRfFLUQDNmnWXC9z66oQeaQqcuzwwKn8KNnZi2fTcHBsJf6XJ5Q7B3cSetD7P2utvBaehse9KLtwvFU4iG97b1ySF9Xdg6WM6LEW",
      "4i6wNZA7wqAdnyhq7Gj9mZnQbevuAAjhmGPpByjv9SYUKp84gXc3GMi4w4TBTdUD9dxhbMntCMAZKMZpd7ybz442"),
    ("8m89eZRfFLUQDNGWs4rMYvGgxzhcGLvZnGP7hNbKMPNx46xZXn4YLw6CwgAGPoTDBxjh4Z56ubaugVnKnrqSLwBBwZu7MZJeVzMo3ebaP",
      "246sasPpxXt8pzd4PYmgKhYMsjA1YR9MfLEVcVhuUfPmgn7cWn335LKcrhTfLBtLhABcnAJCbVd2pzXvHr4cByk3"),
    ("8m89eZRfFLUQFWdhFwLHfucCfHGRec7WpdL2q5BaoQZLSzCH58dmaHxhawYSAfYUkUA35ZhSst8wGhALCRXnH68fo8R83B2xRupN1mepd",
      "5u6jgMK5PXUAQdBvCccJa7Y5NGUqBh9f8B1bEwdXuKvnvgUrtdqnYK3U8yBg5narNxK962rNXF3NTEWwVhfUdRL1"),
    ("8m89eZRfFLUQZw197RpJXnUoQCQkDXR7RPNTGqQZ94HZ3vpKaBtL7PVoMYoEWMdYuN8iBdCTGuUtRV5foq3TTJBqMz2x5rVthGSQ5VULe",
      "41XCYmUM4GHh1dLjzdGrymr1iWqQVoKfZBfDZW8bTJaynoJzcBrQHXXsEj9xM1T5um5ExzGW8SR2XFx2xMQdUfUr"),
    ("8m89eZRfFLUQ27kizVKFUqfDDKcm1zhWHNUkRDEsbuEdqmYERXNdha6JdqKZK4g9oXKpt1idvrxqFiSHU43Uf61WeqErVMA1BHSGg1Qey",
      "3ZA9mLgyhkocQAS5sGx32r2YVxZ2PEvdyvgfptcPrdnBENumjXSwnc7UC6Dev1cdKhF33hCm8vUeFFgBGE2mNrW9"),
    ("8m89eZRfFLUQDjPt9NjmFXXhhtoMyChQbwTYQL8E93g9GZWwxemmBcaGLfcYaeYDrNRsFgqjssKQw4wsJJrhqWr1DAtSmi6zGdhECVEYS",
      "Lidmqu1KaV4dxU7BsbtzXUNNkjybuBJjvc5jYvTGcnn1FvEd7LQuVmFevoHx79jqJXTmw1zrrLRpHhz5mZYyFeS"),
    ("8m89eZRfFLUQ8EZ7C9B7DiwWhqU3qt897kf2Ew2aUNdHU5VS7YsvZ5RZXDLrMZAZqY3u8yWot1veZqoi7LaPcP1gnz3aBnKDoDkyyLA3J",
      "3Ya13XKR6myEh1eV6ExsWe5uVQ3Rgtvv39cDT8w4jSUFJa9QoncbS6xXk3FCjrANWxPGwbs3io1mW7mZZyeYqfhA"),
    ("8m89eZRfFLUQLZ62HmBHmpd4E1ymE78LkmYfqUp3gnre2gQUVTfLWjMqeipjt4HC4E9F7hqGZFsZHuknowCoVbkF3DTecTZdARt89UMGg",
      "3KFcgHSv3D2cmYnsVg5ipHCFEUJfCuLQwgxk3NaoE3mcfUAbrdyzoAktSXLQaJnPsg5gaPR48KTKMdMZ8iMGqRE"),
    ("8m89eZRfFLUQENshCADmKULXuu68AzWWMn6QfC7BvRQeDgPpm4rNr2G3kNVrwinKM3gaMgFoutqamvVGkyzF9A8LYRzETfVGvTSigDosi",
      "2TAXfFFjeeQpfUAGKQZPsxRrhE6nDJ6HAD9wD2TvdzLpe64sVpztDYs3wVZECVbk9gV5jqsJpRf4b3FUDTJgizKN"),
    ("8m89eZRfFLUQGBTnMK3H7JKpNsEobBewdnWpydYimLQ5BhLn9Mfab4Y62ofUTYV3PWYXENSq391tyqfqj9Je6EYp16E9NHnwAmFhadpit",
      "3YTJAMuKRMjBRXy57KJtiMK9Hqhqt99ybKxJ2uVUE5CYnByRLjhJV9ZGYJqegp88Qz9bpJYusmjzpCAFTBhyRa9N"),
    ("8m89eZRfFLUQ9n3hJXa87UwjJiwKhmcdwGaFU6bg2ebGquu4QbgDMSaDtPwgXeQQ4BfvvShRGBkKr7H3QAvgNXiyt9he95VbQZewS7U6J",
      "Feur98gUBduda5HFLzpyiqtyuhnxAugWHdmNxAWDQYtr2f4DXprva3ehGkGcH9fTA1mg46NW7UB2uoCKwfWPR3k"),
    ("8m89eZRfFLUQCdf4YXosG96mMzZ4qondf4nNnqtAGSkNn1EcarWkzPR6Ya78Eh112BMbei7AmFR2yHWrFao7eS3MTMd1YGAC7WFwSALvT",
      "12AFtYWgkXsCQmGv6V3TJSU7TzgUEqCsxGsP3hgWhNwXXi4xaetJ4dA1viE6h5HazUAXeCFAXEwFEXM79WhMjq43"),
    ("8m89eZRfFLUQYFGD5NtPANddtP5wixxA8ASKxs2FAvkYo6AiBhDyDHtGbf2XEDPq6fW1M2CUQJ1v9V3PQyG398h4re1VPKnZpmzB7WmVG",
      "31Zf4HJ4STU79LLW3yMX5a5NUrqWh56vodKNAkyZZh6BgCkxc5LeuS66iSm5cwWRVEJh1u3gwukiP4vkToH9jPU")
  )

  property("getBlock message should be deserializable from new code") {
    forAll(newGetBlockMessages) { (message, id) =>
      val messageBytes = Base58.decode(message).get
      val restored = handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get
      assert(restored.spec == repo.GetBlockSpec)
      restored.data.get match {
        case restoredId: Array[Byte] =>
          assert(Base58.decode(id).get.sameElements(restoredId))
        case _ =>
          fail("wrong data type restored")
      }
    }
  }

  property("GetPeers message should be deserializable from new code") {
    val messageBytes = Base58.decode("2WwnbLbC35xdLqQ13T").get
    val message = handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get

    assert(message.spec == repo.GetPeersSpec)
    assert(message.data.isSuccess)
  }

  val newPeersMessages = Table(("message", "peers"),
    ("AvUkFrQCX6CQZauU1xYo5wu", "Vector()"),
    ("UgNR72JngV5dci1R5rPczEcR3co2DV8u7VFZQYaR2szYT82VLKLrFfsTtkCdfmUr9QLLtweLWxjuZ6PPjMMLo3jDghYt5riTQffwdAX1N7haFfRApDhVXVSLaC3pQCF6rkZp",
      "Vector(/106.84.38.221:49788, /31.69.80.11:56169, /151.148.125.11:41607, /178.247.251.113:63419, /141.9.144.199:16519, /169.172.192.54:31101, /245.98.171.235:32023, /209.188.111.159:45352, /57.180.201.223:20697, /191.183.151.180:60275)"),
    ("6Qf1M1ZiN1ZdEjA21gM5RAPrLvUDAh1ABbft9xk3uC4tR", "Vector(/151.193.12.230:33628, /232.165.127.151:53642)"),
    ("snSypu6duoUNCaMPVzMQaX4CzSV4g6sX7ZNdbP6bw1PPXiadZEJtWAQy2XYkeiF4DKgpNgT4YpUWCRksLabbEvCyMtgMMFvMzC5NGFW9wSeBbe",
      "Vector(/211.39.165.117:13755, /124.177.215.214:3051, /92.35.208.214:20886, /84.167.214.38:32300, /151.240.220.201:31723, /164.164.37.110:48031, /126.106.124.44:40600, /77.247.180.9:17369)"),
    ("G5ytQW3kLo5P9c1Gff1GyLJ3zcVkXYwBNauaQm2KVe9uPTL2woi9K4iTpTNUBR3gMc4NWFJpawzxiajQHSQPV1j4wJd9ikLZ8SsLdJVa2i4m9aLJrQVYzDhTQu6EBHm5WuHn8i4X2bLe6oE98H2RaHJTum",
      "Vector(/201.78.163.162:38852, /120.114.104.175:59076, /231.226.25.254:24576, /96.251.37.150:61875, /36.34.31.188:35818, /144.241.194.33:38286, /203.49.78.125:32266, /235.49.66.38:47817, /99.154.100.156:29148, /138.144.251.153:5506, /70.111.182.94:40618, /109.93.148.201:18276)"),
    ("C8y5uXzRukdw4UrugsdsF3zfAL9xWT9t94r3ZmVpc2J3YLy61PqXCZbnRypXrAgct6WajPAYYUAqopPhbGii4fQDj2cpyjDHrtpXrHJcqTNRzoMBPaQZ4Zb9FKSn84xMGpr5WNF7xCvkXGSWBHTKMBGMhKSHoGFqpybgC",
      "Vector(/128.86.106.111:45309, /218.237.176.8:42572, /122.49.76.158:44333, /174.139.218.102:24152, /180.177.185.22:14018, /76.55.107.205:26658, /97.74.29.7:25546, /219.112.16.25:31359, /11.135.20.167:19017, /94.127.30.65:17406, /223.128.250.235:5099, /3.55.189.32:5885, /46.167.208.94:35673)"),
    ("G5ytQW3kLo5PEC94GKXgSnpPM3pgVYruLdtcukhMdpS4tS6VRN2JJj3agrHHrhrYzCHa9LgwmbQALJNQi3Dzie94aH6LCPzEKko5RsHvyyoA2hpP9AZ7Qruw6oJsyduxZ1sRSDMq9KZ7vSF9m1nDQtJXxz",
      "Vector(/113.208.232.87:5863, /229.118.179.163:60133, /230.228.154.20:32744, /156.147.83.147:21198, /197.193.52.209:19370, /214.69.82.218:7746, /93.219.93.112:2508, /144.195.26.59:52060, /241.183.25.24:23740, /216.195.115.119:25360, /194.56.250.136:4669, /203.212.76.200:47775)"),
    ("C8y5uXzRukdvhfPzvxC75XAg37vjBKB7ufsPHbiqj3LGMvzjZhoUWKvyWJyXYCotVHJ93awiXp4DzLBi6XnLQkPpU4Rov1WbG87uL2V8EQJMxbcGxPaiVjYZVU3kyicA1R1gh9zbY23t4nXeVaM2egUkvdkeQQsB9gou6",
      "Vector(/219.129.129.57:26715, /252.28.38.158:5892, /116.65.110.112:60270, /217.95.19.187:47286, /248.60.236.112:55199, /54.176.97.89:11833, /77.117.197.78:21687, /70.111.55.24:24447, /252.144.87.52:26495, /140.203.230.247:18582, /208.206.73.219:45857, /193.68.180.128:18359, /130.79.29.230:33821)"),
    ("MSBph22sBbkWgXM9aKyYF96WYodpPPk9fUgAT1PceMZmgvewwFeJUBedhXXEdXwDG3vW13UppYF6y5Ddth7sYFByg7cYJ3JmvPEosfVrA7cNJXfbcbZbT7ND4JrpB8nJVbh5sAHad9FBE17",
      "Vector(/135.194.14.62:53287, /236.215.160.195:28889, /42.180.2.43:7474, /242.212.193.187:31418, /33.71.54.198:46311, /20.242.183.207:36462, /63.16.176.239:43895, /47.7.225.216:63500, /135.90.80.16:46729, /103.176.56.19:37916, /43.69.210.230:12746)"),
    ("snSypu6duoUPZWYtMyd8RYeb2UBS4GFggmWidkau4gc3xDZj4gGAAeC5cQJXaeXF9kfEYfb2bbFhDLppxMib9stSvESaCCsLBmoVXbhNMmAdWu",
      "Vector(/220.63.103.103:51933, /207.103.189.244:55540, /234.80.121.227:52679, /76.57.243.80:58136, /107.92.113.215:18613, /247.197.24.11:40542, /4.229.137.246:50595, /22.216.199.176:61374)"),
    ("3wxGAwvVJ1qCWzcN4aA8XXX5Rn8ofGgXCDDLTxjfhwQ7Ui71x6kmSrRzhuHXnUcWrM7",
      "Vector(/166.167.154.61:21663, /100.153.195.128:44106, /165.29.56.49:24663, /61.71.182.0:42058)"),
    ("G5ytQW3kLo5PhQHFo4ni1vLaEcin7JLFihrfL5XVKmiaAgG3wLWW1UvruGZNEJtkZ62xGRs3vxbCgiTwQs7KodwnKNahennVbBcp3U5BhatHq3dnhmo2Z4zAUu6HKabbWvoWP5mdY9mWQCZ9N1tzzvAqJ1",
      "Vector(/160.11.33.74:28771, /240.81.233.187:5166, /56.23.17.81:1005, /109.111.7.214:6156, /222.245.205.107:18293, /145.87.20.219:49860, /112.214.247.148:32927, /126.150.229.152:9602, /232.186.216.133:4095, /109.186.0.167:39702, /205.88.180.75:36165, /249.101.233.178:63666)"),
    ("MSBph22sBbkWJ51SLfS3ryA4k8aho8jdr3N3dKaKwWQnwE2x9Mq7kA8uBrx7UyWEpCrANNjQuNypEYyPnqYr8wYaML73HZVdNSdt3QM3vNMEicZTa6U3bVRPingbN35YKvkn4jekg84LFLa",
      "Vector(/244.120.87.192:58688, /137.82.138.75:6718, /190.233.202.254:49180, /227.32.13.220:18995, /208.102.36.213:32484, /216.166.228.215:20755, /159.221.80.242:31312, /187.209.163.122:2377, /34.127.149.119:12805, /249.114.124.70:10882, /65.95.123.202:20527)"),
    ("snSypu6duoULutGTwzLtiVDCW6GjuqkGDhVXoLaXerkrA2vTh2TDpM87PUhm8pR9ENfEFJrusJ6v4vPtNRmzTPYRr73bZeLUpRDC1yxMyatwTH",
      "Vector(/1.82.219.113:14856, /70.95.57.151:15218, /33.88.5.38:29479, /160.22.163.196:2179, /24.92.113.140:60959, /26.151.30.169:6788, /197.53.227.133:25560, /36.13.231.238:36372)"),
    ("2LDNza6SgYbmZj6CQK5Cr2uPZJ66LQ9YZJxTa3bk5y3Uv3QBF5CpnWBn1ZRfLHXHmEy8PApgPG5Ag93QgnaoG37QtVU5zSQedpkHkhV9ELjHk1nEegrPD8uqdpmL5btcMG2nnwgcMYTM1g3wSSNeUyzcrxq6kvVghmhfCWiHhq3JqQvJdd6jRtc343ziLmvpDSxwRUx9QBtTvkyUYsUgRxdKpWZ4oZ7ZXU5CqBTFtQk5EyXjLw",
      "Vector(/27.234.72.99:55103, /12.62.50.227:1034, /205.97.156.31:6865, /16.119.156.82:48613, /120.250.115.55:19485, /222.245.42.219:25011, /76.233.166.180:5670, /167.210.33.31:30094, /126.41.21.22:33875, /65.244.179.137:41706, /175.112.150.175:47615, /244.56.43.92:26093, /212.188.175.115:50699, /134.98.249.51:18450, /181.23.66.33:56038, /137.152.114.252:33173, /38.104.185.54:61806, /219.203.162.148:16127, /16.131.236.132:21125, /234.238.216.212:31548)"),
    ("755Ho3GbHe16biZGsTEGj9ZHLdNuCkPUFbYoQYsUceyPy32YjjQ1Zcg4Yx8gkcgqdpQmFXwzcVXvwMjhe6sa4K5bCMxNrfvn4fZEubebsXHFFmXiFjpJ3afbkBy4WRNfqoefWDRiV5VE9yiTPNY4jkLstY1c5nWC9GMi8DEowDRKYrNhAWx34n3bbKA",
      "Vector(/156.57.161.39:2579, /99.197.227.50:9373, /87.194.30.50:46201, /49.155.49.21:38386, /160.102.231.219:8244, /242.244.62.130:18877, /227.129.150.146:13708, /238.18.162.63:15100, /190.1.73.141:37196, /216.136.134.175:5263, /140.238.186.97:34561, /153.236.173.154:21943, /60.104.82.73:30824, /88.35.86.240:12060, /219.12.98.144:43061)"),
    ("G5ytQW3kLo5P8Zmk9RGoUPYb1nhj39yaL8toFg5pJ8PMvRiXrwpr95FQcqDu3BpnYhntLUTb6xC7jPS16hoFdu1yxMMVj39G6dpYi9qGqTH6SUhikQdxQk8cmuLm9M8cq3UJnPLQjkk1XnAn8XG55AV1py",
      "Vector(/205.148.115.209:51335, /204.72.179.53:14855, /139.6.228.223:61267, /192.33.91.92:18215, /102.169.147.33:42661, /153.72.198.174:3288, /212.173.238.174:11435, /173.131.127.135:31383, /84.182.74.211:46180, /212.7.150.18:53468, /175.164.21.46:4115, /13.110.147.55:27086)"),
    ("2ob6Q5WF1sAFs9go5EaAVGYovx2FdRmQvJxAnP4uESMhxG3J77qY1v6B891bJWhUnnTJevkJtYCWMjb1qA6nyeGycrEdqxhhhv2q2Jm9jQZAFqwqEy7CiH8QAvs3yWEFkcZ2uQot1aYQ1YuSQi78GsezQ5wzhgvxKvmyHP6nDfserXqrn83KVHAnRr5zSxtzfzfZwEsGKyfAnjciW7rmHYdtFRVrkJeb4TqVdJf",
      "Vector(/149.240.126.215:62370, /95.75.116.48:47499, /124.21.235.253:41422, /83.116.182.134:18846, /86.101.39.227:48634, /43.219.106.53:46050, /248.160.120.193:61062, /184.59.207.249:9387, /250.109.222.205:3670, /100.168.114.254:10745, /83.63.121.63:23752, /19.53.141.74:55667, /0.249.215.127:44145, /39.248.0.6:7995, /99.98.62.32:42165, /194.186.193.124:43392, /178.239.110.67:27441, /252.127.30.157:27219, /28.231.244.68:35184)"),
    ("4zZWMxa18KJK517XD9gfwGEUVRDdfELjCnb1R661M3ejuYV1jrYX2Azu",
      "Vector(/16.128.232.192:44929, /28.119.218.36:19280, /181.218.128.77:8970)"),
    ("3BD5nAPeexyN4qvjUigofz1GMZLaDa4VAdrKnXRWBxUQXiDCeVgXWrrpfQhBjqM3Eo4QhxpxafK9to",
      "Vector(/47.61.254.209:31384, /163.141.145.129:64702, /15.77.101.108:31517, /148.36.14.217:5704, /101.247.200.81:34284)")
  )

  def peersFromString(s: String): Seq[InetSocketAddress] = {
    val peerStrings = s.replace("Vector(", "").replace(")", "").split(",").map(_.trim)

    val peerPairs = peerStrings.map { p: String =>
      val x = p.split(":")
      Try((x(0), x(1).toInt))
    }.filter(_.isSuccess)

    peerPairs.map(p => new InetSocketAddress(p.get._1, p.get._2)).toSeq
  }

  property("Peers message should be deserializable from new code") {
    forAll(newPeersMessages) {(message, peers) =>
      val messageBytes = Base58.decode(message).get
      val restored = handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get
      assert(restored.spec == repo.PeersSpec)
      restored.data.get match {
        case restoredPeers: Seq[InetSocketAddress] =>
          assert(restoredPeers.map(_.toString).sameElements(peersFromString(peers).map(_.toString)))
        case _ =>
          fail("wrong data type restored")
      }
    }
  }
}
