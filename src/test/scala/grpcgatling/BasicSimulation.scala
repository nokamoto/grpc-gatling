package grpcgatling

import grpcgatling.grpc.GrpcActionBuilder
import io.gatling.commons.validation._
import io.gatling.core.Predef._

import scala.concurrent.duration._
import scala.util.Try

class BasicSimulation extends Simulation {
  private[this] val scn = scenario("BasicSimulation").exec(GrpcActionBuilder("/ping.PingService/Ping") { _ =>
    Try {

    }.success
  })

  setUp(scn.inject(rampUsersPerSec(10).to(20).during(10.seconds)))
}
