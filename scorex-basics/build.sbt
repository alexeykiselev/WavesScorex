name := "scorex-basics"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++=
    Dependencies.serialization ++
    Dependencies.akka ++
    Dependencies.p2p ++
    Dependencies.db ++
    Dependencies.http ++
    Dependencies.testKit ++
    Dependencies.db ++
    Dependencies.logging ++ Seq(
      "org.consensusresearch" %% "scrypto" % "1.2.0-RC2",
      "commons-net" % "commons-net" % "3.+"
  )
