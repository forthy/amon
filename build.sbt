import AssemblyKeys._

organization := "com.github.amon"

name := "amon"

version := "1.0-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers ++= Seq(
  "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
  "scala-tools" at "http://scala-tools.org/repo-releases",
  "sonatype-snapshots" at "https://oss.sonatype.org/content/groups/public",
  "maven" at "http://repo1.maven.org/maven2",
  "oracle" at "http://download.oracle.com/maven",
  "twitter" at "http://maven.twttr.com/",
  "java-net" at "http://download.java.net/maven/2"
)

libraryDependencies ++= Seq(
  "com.github.bytecask" %% "bytecask" % "1.0-SNAPSHOT",
  "com.google.protobuf" % "protobuf-java" % "2.4.1",
  "ch.qos.logback" % "logback-classic" % "0.9.30" % "compile",
  "io.netty" % "netty" % "3.5.11.Final",
  "org.jgroups" % "jgroups" % "3.2.4.Final",
  "com.codahale" % "jerkson_2.9.1" % "0.5.0",
  "org.apache.httpcomponents" % "httpclient" % "4.2.2",
  "com.google.guava" % "guava" % "10.0.1",
  "org.streum" %% "configrity-core" % "0.10.1",
  "org.xerial.snappy" % "snappy-java" % "1.0.5-M3",
  "org.scalatest" %% "scalatest" % "1.6.1"% "test"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-Xmigration",
  "-Xcheckinit",
  "-optimise",
  "-encoding", "utf8"
)

javacOptions ++= Seq("-source", "1.7")

publishTo <<= (version) { version: String =>
  val nexus = "https://oss.sonatype.org/content/repositories/"
  if (version.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "snapshots/")
  else
    Some("releases"  at nexus + "releases/")
}

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

seq(assemblySettings: _*)

test in assembly := {}

