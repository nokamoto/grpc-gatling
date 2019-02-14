package grpcgatling.server

import io.grpc.{Status, StatusRuntimeException}
import ping.Message
import ping.PingServiceGrpc.PingService

import scala.concurrent.Future

class UnimplementedPingServer extends PingService {
  override def send(request: Message): Future[Message] =
    Future.failed(new StatusRuntimeException(Status.UNIMPLEMENTED))
}
