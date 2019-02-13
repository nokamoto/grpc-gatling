package grpcgatling

import grpcgatling.grpc.{GrpcActionBuilder, GrpcProtocol}
import grpcgatling.server.PingServer
import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import ping.Message

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Try

class BasicSimulation extends Simulation {
  private[this] val port = 9001

  private[this] val server = PingServer.server(port, ExecutionContext.global)

  private[this] val protocol: GrpcProtocol = GrpcProtocol("localhost", 9001)

  private[this] def grpc(name: Expression[String])(
      f: Session => Unit): GrpcActionBuilder = {
    GrpcActionBuilder(name)(session => Try(f(session)).success)
  }

  private[this] val scn =
    scenario("BasicSimulation").exec(grpc("/ping.PingService/Ping") { _ =>
      protocol.stub.send(Message())
    })

  before {
    println(s"start gRPC server $port")
    server.start()
  }

  setUp(scn.inject(rampUsersPerSec(100).to(1000).during(10.seconds)))

  after {
    println("shutdown now")
    server.shutdownNow()
    server.awaitTermination()
  }
}
