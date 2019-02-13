enablePlugins(GatlingPlugin)

libraryDependencies ++= Seq(
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.0.0" % "test",
  "io.gatling" % "gatling-test-framework" % "3.0.0" % "test",
)

javaOptions in Gatling := overrideDefaultJavaOptions("-Xms1024m", "-Xmx2048m")