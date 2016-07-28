package scorex.account

import scorex.crypto.signatures.Curve25519

@SerialVersionUID(8568952246932352318L)
case class PrivateKeyAccount(seed: Array[Byte], privateKey: Array[Byte], override val publicKey: Array[Byte])
  extends PublicKeyAccount(publicKey) {

  def this(seed: Array[Byte], keyPair: (Array[Byte], Array[Byte])) = this(seed, keyPair._1, keyPair._2)

  def this(seed: Array[Byte]) = this(seed, Curve25519.createKeyPair(seed))
}
