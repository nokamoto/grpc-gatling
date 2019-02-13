package grpcgatling.grpc

import io.grpc.ManagedChannel
import io.grpc.netty.NettyChannelBuilder
import ping.PingServiceGrpc
import ping.PingServiceGrpc.PingServiceBlockingStub

case class GrpcProtocol(host: String, port: Int) /* todo extends Protocol */ {
  private[this] val channel: ManagedChannel =
    NettyChannelBuilder.forAddress(host, port).usePlaintext().build()

  val stub: PingServiceBlockingStub = PingServiceGrpc.blockingStub(channel)
}
