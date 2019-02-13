enablePlugins(GatlingPlugin)

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.0" % "test",
  "io.gatling" % "gatling-test-framework" % "3.0.0" % "test",
  "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
)

javaOptions in Gatling := overrideDefaultJavaOptions("-Xms1024m", "-Xmx2048m")

PB.targets in Compile := Seq(
  scalapb
    .gen(flatPackage = true, grpc = true) -> (sourceManaged in Compile).value
)
