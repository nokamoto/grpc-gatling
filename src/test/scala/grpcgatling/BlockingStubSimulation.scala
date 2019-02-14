package grpcgatling

import grpcgatling.grpc.GrpcProtocol
import grpcgatling.server.PingServer
import io.gatling.core.Predef._
import ping.Message

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class BlockingStubSimulation extends BasicSimulation {
  private[this] val port = 9001

  private[this] val server =
    PingServer.server(port, ExecutionContext.global, new PingServer)

  private[this] val protocol: GrpcProtocol = GrpcProtocol("localhost", port)

  private[this] val scn =
    scenario("BasicSimulation").exec(
      blockingGrpc("/ping.PingService/Ping", ExecutionContext.global) { _ =>
        protocol.blockingStub.send(Message())
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
