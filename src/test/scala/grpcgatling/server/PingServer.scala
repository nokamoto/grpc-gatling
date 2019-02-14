package grpcgatling.server

import io.grpc.Server
import io.grpc.netty.NettyServerBuilder
import ping.{Message, PingServiceGrpc}
import ping.PingServiceGrpc.PingService

import scala.concurrent.{ExecutionContext, Future}

class PingServer extends PingService {
  override def send(request: Message): Future[Message] =
    Future.successful(request)
}

object PingServer {
  def server(port: Int, ec: ExecutionContext, service: PingService): Server =
    NettyServerBuilder
      .forPort(port)
      .addService(PingServiceGrpc.bindService(service, ec))
      .build()
}
