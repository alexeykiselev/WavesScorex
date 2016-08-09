package scorex.network

import java.net.{InetAddress, InetSocketAddress}

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropertyChecks
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.encode.Base58
import scorex.network.messages._
import scorex.serializers.ScorexKryoInstantiator

import scala.util.Try

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

  property("GetBlock network message should be binary compatible") {
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

  property("GetPeers network message should be binary compatible") {
    val bytes = Base58.decode("ESW7cwVVPERD").get
    val restoredMessage = pool.fromBytes(bytes, classOf[NetworkMessage])
    restoredMessage.contentId shouldEqual 1
    restoredMessage.content.sameElements(Array[Byte]())
  }

  property("GetPeers network message should be deserializable by old code") {
    val content = GetPeersMessageContentV1()
    val contentBytes = pool.toBytesWithoutClass(content)
    val message = NetworkMessage(content.id, contentBytes)
    val messageBytes = pool.toBytesWithoutClass(message)

    println(s"${Base58.encode(messageBytes)}")
  }

  val binaryPeersMessages = Table(("message", "peers"),
    ("AvUkFrQCX6CQZauU1xYo5wu", "Vector()"),
    ("2BnhEdocjSk3e3FBezFKKm17KGYghZtPSubrEjXvoBiv9ATRbPfT89ycjKeS3vWaXLqGoWQTLdPRqudsEsJErpmcrisob9VC7oRM",
      "Vector(/201.203.149.184:9699, /103.67.119.192:61156, /74.23.143.25:63286, /27.121.227.48:28949, /83.250.230.114:23446, /182.116.246.31:45040, /176.67.185.97:284)"),
    ("3wxGAwvVJ1qCRnmbVH84iJ8HMSPAFjfFQMKR6SvyBXrhXdWiN4uA8LmVBnaSYcR4Hdu",
      "Vector(/170.38.5.213:61563, /94.252.234.9:58425, /127.67.38.126:29146, /220.112.6.120:31412)"),
    ("5UvZxMdtYBmTvHrDgC1X8aNPjLWCfznuDKGx4umEjtwGWFk6JMiaUncS59CmzpLWPAnC2hrD1dud1rLfNiyzWeucba2qbb9E3spRQcVLKcoRAK1dzCYocuD5MKNmvHVbSyQseTFDxbcox6p3Wmw2cPMfgRgKStvkzkyQuqNfDfffqNLXrPwFqZjJaqBsRfoDgD8E6i",
      "Vector(/194.194.115.213:54103, /243.21.39.164:3129, /151.173.129.232:13972, /46.44.251.117:36308, /41.52.156.19:58652, /43.230.169.129:4932, /220.11.248.239:17467, /182.160.203.63:5954, /3.214.72.43:59159, /60.12.122.64:57766, /118.229.10.86:3225, /12.143.157.73:4060, /165.112.188.223:7398, /173.191.3.20:64087, /122.54.125.250:13824, /100.99.208.203:1879)"),
    ("8KrbStPLwvA7XLGojhL56QogoBzqbMKBKM", "Vector(/30.174.21.224:22368)"),
    ("4zZWMxa18KJK5v13CXBxxidNtpAwPSSs9GRr1FCj9WiU5aMXec5JDFk3", "Vector(/210.192.195.69:39787, /71.171.210.186:63718, /209.218.5.188:26984)"),
    ("2BnhEdocjSk3atZRPXPtMxP5yMwoqYCFgbzppJv4nKUmiUzkfBAAj3K6DSbXz1WA2SWpd8sjQrJAdsM816KgpKuYUaag6gxuX9Zy",
      "Vector(/126.210.230.129:64409, /224.75.152.5:38920, /173.220.165.40:58328, /79.37.181.118:32027, /107.250.214.38:12310, /29.194.76.35:62067, /56.36.31.62:41768)"),
    ("2cB9xzCpK3uTvnp9ku5XwsbKUAqtdUdB9MG4TXDSXsLXHWhULJhrv3FhUawqZn1GqTkd6nrskPXGQ6BfARRNXQvq9",
      "Vector(/8.239.139.164:41137, /60.108.62.43:51725, /16.18.197.80:54777, /246.102.88.79:53370, /117.129.180.213:1772, /34.136.237.109:49652)"),
    ("2cB9xzCpK3uTyH4rArpDNiZRZx1Wo5iuXXd3NjuHjm6E3z8TSjvPf1Z1CXHJJbpmaHDr739vhs5G2a7cQfPiRtT3d",
      "Vector(/209.13.244.142:55745, /10.252.17.226:54471, /49.247.253.143:28656, /78.82.187.3:14504, /116.232.203.13:45259, /155.199.105.237:10712)"),
    ("UgNR72JngV5eHGeFtu6gFDddZxZ9efsnfczjx92dNsvPoJKRwpRF8gSkd2siprg5VWbUxg55Bn1cBhcCw8mw8eu4kseFcHsWcmPKZRaMGh7i9z7t3nnUnFcZKyoi6GcpJ16y",
      "Vector(/212.37.154.118:12841, /7.70.5.102:29007, /146.82.130.66:8214, /182.53.205.55:35764, /207.29.61.62:8890, /43.118.147.213:23707, /15.177.164.193:45371, /147.114.111.165:43238, /45.59.160.21:55357, /112.16.65.81:36882)"),
    ("9DtsLooo1Lxj4HJs481erofNksLZRoCVHmDvHqQLPgCSCsFFoxDJaNQfNJ2rFSzsEaz2PnWZtTxbyXQVMB6tG23hCFqN3a31Ln6yBbXi3ChPycT1C5Byf8itzwH12JRdCPqPHShAUXi4ZSUkgBFQ7CSx827oQe8jF53ZAMqc699esvS8",
      "Vector(/194.24.237.218:24612, /203.136.154.240:47758, /71.29.116.20:56799, /232.206.245.56:19864, /7.150.123.34:7484, /8.173.66.209:21306, /62.64.165.177:33093, /19.177.29.36:17365, /16.174.5.100:26174, /62.199.112.3:47692, /162.206.156.241:65230, /217.133.140.89:18270, /166.173.17.78:31541, /235.184.66.41:11205)"),
    ("6Qf1M1ZiN1ZdRCWBGnZUNZjtKpy6NLMS4tsQeDCC7b5QD", "Vector(/133.178.54.192:47534, /18.223.75.131:64802)"),
    ("MSBph22sBbkW3UwzzZxdYaNtoS1uxArL8QJ9RyZkyNWMYxmNeScG1BZCP9zFHLbJwf3TwbLPpyHvABNiUvWbjFq5LYHRHw2CqbB4GMbpqjtoJ7G7ARw7HJ9nXV5UQhr8DRwtTWuAsBkVzUE",
      "Vector(/238.23.232.221:55264, /123.176.112.173:44850, /0.70.75.183:4531, /86.84.11.181:53712, /232.205.151.251:21289, /147.61.70.150:5487, /49.58.166.125:49880, /108.5.26.127:25426, /164.135.32.71:39851, /187.162.178.159:23659, /40.29.240.246:63103)"),
    ("Hw9xsmPPFZBxY9QcS9JVrwdYAqbX37jDv79biSAtjL2KcyZkuLGkiy1PGTZJdZpEaLhQGA7RHAwRGqrcknTE5qwNyfvxhBnG2KSRGs9R1S3gLC3ND2h2wptYAUU4wQUQpTWPupjdiT8yk5smDinwcX47mnCBQkefydLauomRLR1PY7YjdSWyT2XRAU8eU3MY8i1XKCrFWZBWFJL4eJqN8htDRyd1DeRU3aZhCvy2J3rYJqhhY1i1zzZfdRXXSn7xNj2xHMdSuynsbs2yUxJApgGNs53UBSQwURzNjkkc",
      "Vector(/126.140.254.141:12634, /37.72.150.216:56915, /158.244.96.188:55632, /236.52.116.69:57616, /14.154.138.190:55528, /28.112.97.78:38770, /214.39.9.216:2269, /25.216.96.49:55293, /105.58.119.68:434, /58.252.235.102:16482, /81.127.224.149:6341, /197.240.161.206:36317, /100.187.169.241:39297, /72.94.30.177:35360, /163.64.24.228:15831, /173.5.197.197:32874, /177.131.158.202:54731, /21.238.23.137:43296, /127.207.240.60:60359, /41.163.106.140:32348, /1.100.4.14:7334, /107.190.135.197:59769, /20.213.19.23:64173, /172.80.36.206:49784, /195.29.55.121:28141)"),
    ("3SfhRwGoi3TREXpmtyYQUYwKLS6zehF3w7bDeS5QgfhJQvGXgiT8uK9PXPoSLMGvobgqvPDdCAPcMMNK5AbAgw1kb9oPFr36huobvXs289DtWgDTaPp44nY6Pa55MRFEdgjg5czYmwFMB4ySsiowytBnMKkcBzKZKHoX1TRU9Vv124ZgYzo6QX8GUG3fg7dkN1ixK2dG1hnRGw457DD91UcvDsjm",
      "Vector(/198.63.201.208:37084, /110.40.173.206:64557, /211.114.119.247:6607, /161.80.20.166:6562, /243.25.18.88:63515, /215.2.234.26:20198, /45.61.145.247:44793, /73.48.129.246:38533, /110.93.95.14:3785, /245.98.2.81:1426, /56.103.12.198:56549, /144.235.151.123:13107, /232.243.226.195:63448, /13.170.11.86:12567, /26.114.9.93:5110, /85.190.9.66:62896, /15.143.56.249:57233, /154.138.45.253:1672)"),
    ("G5ytQW3kLo5Pja7fRYHc7cujmmUUxuBfh1KFLZQMP2S8F7YfdECfZCmb31QKWHdgjxaLzgrgdxi4XKKqQx6TE8c3vPpq6hkrmrWjSysipRRPwz6Aa9qU7fwMrdA1i24aA79pnFGyUJVU5FCNeAa8U7mhWD",
      "Vector(/0.96.66.165:401, /35.190.9.117:53079, /138.251.166.13:56558, /147.219.88.5:58845, /87.213.76.183:37144, /218.43.223.79:8174, /105.57.144.67:60087, /125.6.33.42:56104, /248.13.35.118:46462, /241.48.187.59:43320, /171.46.46.29:55544, /251.132.215.183:26270)"),
    ("2cB9xzCpK3uTw1sdbDLEupTWejiSZkTqGiYJtfeZF5hzLpiWAxMaWyq858pebFW1c5H6fkcKPmY8EaZTChQ43N64P",
      "Vector(/148.4.249.35:24656, /185.120.56.225:62198, /213.18.233.139:15313, /29.30.195.66:8878, /163.185.9.125:61831, /3.31.4.190:48512)"),
    ("3BD5nAPeexyN7dawy8cfEjhNdRpTK2WpkVRhp5yJoDr9R6TY8rcLgARUSL43GGQZfpaM59J5FgSvbR",
      "Vector(/63.195.255.5:10246, /73.132.67.10:56539, /228.63.153.250:25536, /238.137.32.121:25368, /194.167.240.246:32216)"),
    ("2ob6Q5WF1sAFpB88aBbx4CiYRfgeKXY3MnLbPcLdvTq4vCjNQai8UebnQSG58LbvQSf3bMj2qVqEx1MRWZv5t8fvUkSarD3a6XbE9zGJacMKBi8N53Ygbx6vpkGT6M8GTe4XNP1MwhwcZgrstgFYQYoTmQNsqEEvQMpNNVg94bNggHwb2y8KPB8r1xZrTi3oyHvAaX6ZGLFmrUu3ysmJdN6NLbFU48snWSmrFW2",
      "Vector(/125.12.191.215:8079, /76.111.224.51:46466, /173.20.41.166:30514, /171.178.165.180:17126, /188.140.51.21:56869, /108.171.230.240:44821, /127.195.73.70:40429, /182.201.2.58:20265, /87.194.144.250:28557, /161.110.204.224:62107, /20.147.64.127:1187, /125.128.143.180:24997, /217.104.80.235:18160, /177.207.154.200:57494, /224.225.212.248:14993, /205.232.46.127:46721, /240.157.57.178:29707, /44.151.215.53:25873, /230.69.10.187:13651)"),
    ("PwMgGEj8NaCddwJYbCpnvnXcZLztJwmJRohpytLbzcJKPJDsm7ojK7sxLHk3rBJsrDm2yhZC5XYaUZZRtWHkhxjxm66x45Ba6TnaM95Rdb1sNe8DN2JaH6165LmUbwYdYBLs6VRhMCNNSJRYBbVDetJDRTFQgSsjuN5nQoG5gdHPPuXQELDbr5Sxv82JWy1FCGDpuPdQt1E5HxxP7kCS2BZH67WLoZi6Csvz242NTmXSjbEBzsjcJGXoTAzXK7Kv2y1bNyYe8Xn7u9Cv1Fd65NT82RNW7",
      "Vector(/152.231.93.195:39505, /43.94.187.141:53105, /244.86.134.213:29476, /77.224.60.13:3510, /221.47.207.28:17220, /79.157.228.100:47083, /75.30.95.51:13227, /113.189.134.66:33687, /23.105.219.37:38532, /18.225.161.177:62105, /39.223.138.176:51981, /100.211.49.253:61576, /89.155.8.36:36586, /62.39.229.206:34233, /118.235.81.96:14213, /94.69.166.210:9614, /35.222.173.109:489, /247.241.22.197:27825, /249.190.146.109:52249, /57.204.134.85:52062, /178.250.10.255:50839, /43.206.63.3:44463, /18.183.11.175:41171, /96.255.26.106:38844)")
  )

  def peersFromString(s: String): Seq[InetSocketAddress] = {
    val peerStrings = s.replace("Vector(", "").replace(")", "").split(",").map(_.trim)

    val peerPairs = peerStrings.map { p: String =>
      val x = p.split(":")
      Try((x(0), x(1).toInt))
    }.filter(_.isSuccess)

    peerPairs.map(p => new InetSocketAddress(p.get._1, p.get._2)).toSeq
  }

  property("Peers network message should be binary compatible") {
    forAll(binaryPeersMessages) { (message, peers) =>
      val bytes = Base58.decode(message).get
      val restoredMessage = pool.fromBytes(bytes, classOf[NetworkMessage])
      restoredMessage.contentId shouldEqual 2

      val content = pool.fromBytes(restoredMessage.content, classOf[PeersMessageContentV1])
      val addresses = peersFromString(peers)
      assert(content.peers.map(_.toString).sameElements(addresses.map(_.toString)))
    }
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

  property("Peers network message should be deserialzable by old code") {
    forAll(PeersGenerator) { peers =>
      val content = PeersMessageContentV1(peers)
      val contentBytes = pool.toBytesWithoutClass(content)
      val message = NetworkMessage(content.id, contentBytes)
      val messageBytes = pool.toBytesWithoutClass(message)

      println(s"('${Base58.encode(messageBytes)}', '$peers'),")
    }
  }
}