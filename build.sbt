name := "scala-playground"

version := "0.1"

val commonSettings = Seq(
  scalaVersion := "2.12.6"
)

lazy val root = project.in(file("."))
  .settings(commonSettings)
  .aggregate(benchmarks)

lazy val benchmarks = project.in(file("benchmarks"))
  .enablePlugins(JmhPlugin)
  .settings(commonSettings)
  .settings(
    javaOptions ++= Seq(
      "-XX:+UnlockDiagnosticVMOptions"
      // "-XX:+PrintAssembly"
    )
  )
