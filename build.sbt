ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"

lazy val service = (project in file("kafka-learning-service"))
  .settings(
    name := "kafka-learning-service"
  ).settings(
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core" % "0.14.3",
      "io.circe" %% "circe-parser" % "0.14.3",
      "io.circe" %% "circe-generic" % "0.14.3",
      "com.softwaremill.sttp.client3" %% "fs2" % "3.9.5",
      "org.apache.kafka" %% "kafka" % "2.8.0",
      "org.apache.kafka" % "kafka-clients" % "2.8.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.slf4j" % "slf4j-api" % "1.7.29",
      "org.slf4j" % "log4j-over-slf4j" % "1.7.29"
    )
  )

lazy val models = (project in file("kafka-learning-models"))
  .settings(
    name := "kafka-learning-models"
  ).dependsOn(service)

lazy val root = (project in file("."))
  .dependsOn(models, service)
