name := "starchat-migration-tools"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= {
  val ScoptVersion = "3.7.0"
  val ScalaJHttpVersion = "2.4.1"
  val PlayJsonVersion = "2.7.2"
  Seq(
    "com.github.scopt" %% "scopt" % ScoptVersion,
    "org.scalaj" %% "scalaj-http" % ScalaJHttpVersion,
    "com.typesafe.play" %% "play-json" % PlayJsonVersion

  )
}
