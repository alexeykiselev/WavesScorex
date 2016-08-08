package scorex.network

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.encode.Base58
import scorex.network.messages.{GetBlockMessageContentV1, NetworkMessage, ScoreMessageContentV1}
import scorex.serializers.ScorexKryoInstantiator

class NetworkMessagesSpecification extends PropSpec with PropertyChecks with Matchers {

  val pool = ScorexKryoInstantiator.defaultPool

  val binaryScoreMessages = Table(("message", "score"),
    ("7hApEakrcNeNEeYHeGQ", BigInt("1")),
    ("5wa9GaFKZT4Koyt6t7pPjzpxt1pC5M", BigInt("9223372036854775808")),
    ("27vjCghrX1U2uj8EYt8Z1NquKwqfL", BigInt("9223372036854775807")),
    ("ko3Tvh8VhW9nugNPc61PWExF", BigInt("2147483648")),
    ("4LJDsqz85fmg3BighrkDTXNpGP", BigInt("17592186044416")),
    ("27vjCghrX1U2sdxWFRGwWEvdiVv2b", BigInt("4035225266123964416")),
    ("7hApEakrcNeNMZujrDe", BigInt("17")),
    ("7hApEakrcNeNFi9QehC", BigInt("7")),
    ("ZLDu3MmwdNZirwHzHPQLULqPrpF2Kecpit7", BigInt("2376844875427930127806318510080")),
    ("2fFrwoqfhbvS8jyB2LDAshAHFsLrLynV5", BigInt("38685626227668133590597632")),
    ("7hApEakrcNeNS5UWzRm", BigInt("24")),
    ("7hApEakrcNeNYeGTvUp", BigInt("33")),
    ("7hApEakrcNeNH9UwJ9z", BigInt("13")),
    ("pw6uPnZsmYvAzCJ9DPTzX5mkm8DdrE52VZ9SRWf", BigInt("664613997892457936451903530140172288")),
    ("BrsGx1MbABres4QPdUohRxDGrA2kfkGgPP92nB", BigInt("498460498419343452338927647605129216")),
    ("2fFrwoqfhbvSERuxibpEioPoGdhf4y687", BigInt("43521329506126650289422336")),
    ("3ThqvhQhN7YBZJEodzAXB7bUGMpjA1bVVYCuM", BigInt("1379203853048313588828413087449088")),
    ("pw6uPnZsmYvDwFh4zEjwJw1iCUr9DYWJfinndeP", BigInt("23926103924128485712268527085046202368")),
    ("3ThqvhQhN7YBf9kH6MVeqYuDHBJGg8RTT3Daf", BigInt("81129638414606681695789005144064"))
  )

  property("Score network message should be binary compatible") {
    forAll(binaryScoreMessages) { (message, score) =>
      val bytes = Base58.decode(message).get
      val restoredMessage = pool.fromBytes(bytes, classOf[NetworkMessage])
      restoredMessage.contentId shouldEqual 24

      val content = pool.fromBytes(restoredMessage.content, classOf[ScoreMessageContentV1])
      content.score shouldEqual score
    }
  }

  property("Score network message should be deserializable by old code") {
    forAll(Arbitrary.arbBigInt.arbitrary suchThat (_ > 0)) { n =>
      val content = ScoreMessageContentV1(n)
      val contentBytes = pool.toBytesWithoutClass(content)
      val message = NetworkMessage(content.id, contentBytes)
      val messageBytes = pool.toBytesWithoutClass(message)

      println(s"('${Base58.encode(messageBytes)}', BigInt('$n')),")
    }
  }

  val binaryGetBlockMessages = Table(("message", "id"),
    ("WYJKUwVobNMa2RNi4mYa", "EE"),
    ("4eZo4FWa62okTqpFSXSokebao3cyzK7MLqDfcRy4g", "3QXbymfDbP3VrF9SSJdAWTFz"),
    ("CtPwSFfHusc5XDNxtgQe7F1gWcYj8GePqtP6b1K3g4MUhiuSfRWtK", "3BzM7mAtiJGvf5hA596R42zP8xetxUfizZEB"),
    ("2U8Wcv26qXL6aQfh4qLYpJgvzQnCwryN5FSkLeWuuQBeD4xwsYKmbaxhf5t9USCKSgQozg4KT2", "VHkorSVzdCGuB1tXgr33d5NxXSoiCxTTyqaDXixe1oTzZyszfvdsXkwW"),
    ("5nQ6w6Q9djTNB1Axr8xSeskLqKbSCC1J7fZWFC1tgoTzwqxA2UzmRzY3nzMsznM5gqWMU4sotsRZqgEPmNNmYr",
      "2HsaqAA62TuuGqnqWFBaGxuvfMaAi4mk7iVC7ydZYBSPbf8GS5xK61iYPzKLfvwgaDAn"),
    ("T8FVtA1A4vrkRXtWDPBYLLeZK2BeyzH74MuayiFuGMur2ZLdo35UNjmPeZW71", "8oCJ2LhmSXFaJNw7Ej9EqxjNPXHW8jQwzLb47DECvEK"),
    ("5nQ6w6Q9djTNDCNXsVang7R1K1ZQ1dwXXQz6cv7aNMD3XHV5Cmn3HAeUzZ58HwCN5rkdcQjbRs6LrYCFc7d2oG",
      "16q2hEb4F3MFcMJuycZ2jAzxpMRFFwhcwFndoNdhHPte1nxc6cEpv2iVkMrPLc6NmrPG"),
    ("cPT5GF2jrmNaZWtiZkGHsJYBfAVrEDn3Kji1JrsJ48tAqkQoGS", "CgLKm3yFUocb81aaeFBfpLtMUwi9Xgqv6"),
    ("2cB9xzUTdc3buj51cQr5MARpAGavG8UAYSYoq8U6A82hnkd7tu8URXmwZDbjRJWeh4K7Cc2Zmn7c9GWiAotNDzCyT",
      "Xiyzwmvkd4FfXK4zsxe9CwVA35JX44Nsrpx6iAv1hJFAboP4kc7H9atdTDZWv9rsrXm1Gqb"),
    ("3P8uNoEGBD9YbyZ3DCTHBzHAQ2VPvW5rpWdNvcwVuES82hHrriBenh5hoyLMQdDZxVgdm1JfgAVfcjpexzkrpj9p3NZDW",
      "bX9JqjQrxBxW9cqTPTinC7RPjTJ31EajYvfeAuq6dvYzWEedAGYsjoZPj4n8yzDzXWvgHbzddKA"),
    ("2E4dL39U7ajqrZNJmYPwtcGaFhhcFDoHNkUL4xeUaidh", "HSrQHgaQqbem1pGk5SerWu9J3"),
    ("2fFrwop3y8JVrpiHPJ62zAXREeqMbR8Sh", "167YGmSR93Ljxuj"),
    ("fjRjarS9ZpMCFMkzXH52K4VZzSapJ6iUDKJ2M7GhfKRVo2VkCMjnE1NB73awyB88b", "8M9jH42WjEEmPtgASsBwEMskKxP9fYGvf1ZT5xHqCUTpt6ao"),
    ("2pMVq78JDNK7ijtY9Ek58mtShcy9Ju13BiXt3teXnCUvnY6B", "HZG3s7V3o5dJZDPotcoqqhzhb28Qym"),
    ("jPjhGfxRiwudKWbLceRkmnofYknQH9Xe8UW3MUa8G8kYNfxJUnUkdq8goSJBkmm6BXe6S8YF1mTiU82F", "UrFmKKZnyCp6VRbJdk7Jpg8ZgVCfGP8wMZzF2wC9wxAAbmfNZ7zdqpSeEc8vDUf"),
    ("ZLDu3METaHPV7fTERXfAExURt7tAhYQCC7J", "Gs6w7vqsGTS8LrboPE"),
    ("6Qf1M2UNg3v7ChM8MytQMP9QEGcYekPvPEPCQt9u2CJQW", "2mfepGHa8igjeDBCCKcXEUs4THxA"),
    ("pw6uPmmCZahakubws3PvW9wfjUXp5LJJKz8j8JA", "11pEtok3d8UX231zwFL3p"),
    ("6jJyRe89PWxPBFNCPe6p1EFosjHbea3BY1wT4qNTH9rbeZ1jZkLZUYE6Zk3wmWe6sHmS1etAnRraNvdMvXJhEHD8pK8iMcEfjXEsTYZwpVA7HRc3Nhda",
      "3NJmZe8cae42D3r4xyCkVCGyZUp6hZe2fU5ZfpvedJka93hKBp8AikpqRbmAEkQsu3B1rTt86mtEiaMEUYEdhCrq8Ct2b3p4yMa")
  )

  property("GetBlock network message should be binary compatilbe") {
    forAll(binaryGetBlockMessages) { (message, id) =>
      val bytes = Base58.decode(message).get
      val restoredMessage = pool.fromBytes(bytes, classOf[NetworkMessage])
      restoredMessage.contentId shouldEqual 22

      val content = pool.fromBytes(restoredMessage.content, classOf[GetBlockMessageContentV1])
      content.blockId shouldEqual Base58.decode(id).get
    }
  }

  val bytesArrayGenerator = Gen.containerOfN[Array, Byte](64, Arbitrary.arbByte.arbitrary)
  property("GetBlock network message should be deserializable by old code") {
    forAll(bytesArrayGenerator) { id =>
      val content = GetBlockMessageContentV1(id)
      val contentBytes = pool.toBytesWithoutClass(content)
      val message = NetworkMessage(content.id, contentBytes)
      val messageBytes = pool.toBytesWithoutClass(message)

      println(s"('${Base58.encode(messageBytes)}', '${Base58.encode(id)}'),")
    }
  }

}