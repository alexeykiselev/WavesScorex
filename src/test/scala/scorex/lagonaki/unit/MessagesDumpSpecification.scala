package scorex.lagonaki.unit

import java.nio.ByteBuffer

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.encode.Base58
import scorex.lagonaki.TestingCommons
import scorex.network.message.{BasicMessagesRepo, Message, MessageHandler}
import scorex.transaction.History

class MessagesDumpSpecification extends PropSpec with PropertyChecks with GeneratorDrivenPropertyChecks with Matchers with TestingCommons {
  implicit val consensusModule = application.consensusModule
  implicit val transactionModule = application.transactionModule

  val repo = new BasicMessagesRepo()
  val handler = new MessageHandler(repo.specs)

  property("Dump ScoreMessage") {
    forAll(Arbitrary.arbBigInt.arbitrary suchThat (_ > 0)) { n =>
      val message = Message(repo.ScoreMessageSpec, Right(n), None)
      val bytes = message.bytes
      val bytesString = Base58.encode(bytes)

      println(s"$n: $bytesString")
    }
  }

  var arrayGenerator = Gen.containerOf[Array, Byte](Arbitrary.arbByte.arbitrary)

  property("Dump GetBlockMessage") {
    forAll(arrayGenerator) { a =>
      val message = Message(repo.GetBlockSpec, Right(a), None)
      val bytes = message.bytes
      val bytesString = Base58.encode(bytes)
      val aString = Base58.encode(a)

      println(s"('$bytesString', '$aString')")
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
      handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get.data.get match {
        case scoreRestored: History.BlockchainScore =>
          assert(score == scoreRestored)
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
      handler.parseBytes(ByteBuffer.wrap(messageBytes), None).get.data.get match {
        case idRestored: Array[Byte] =>
          assert(Base58.decode(id).get.sameElements(idRestored))
        case _ =>
          fail("wrong data type restored")
      }
    }
  }
}
