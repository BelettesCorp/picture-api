name := """picture-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0-RC1" % Test,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.10"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


//fork in run := true `