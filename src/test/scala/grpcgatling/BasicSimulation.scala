package grpcgatling

import grpcgatling.grpc.GrpcActionBuilder
import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.session.Expression

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

abstract class BasicSimulation extends Simulation {
  protected[this] def blockingGrpc(
      name: Expression[String],
      ec: ExecutionContext)(f: Session => Unit): GrpcActionBuilder = {
    GrpcActionBuilder(name, ec)(
      session =>
        Try(f(session))
          .fold(e => Future.failed(e), v => Future.successful(v))
          .success)
  }

  protected[this] def grpc(name: Expression[String], ec: ExecutionContext)(
      f: Session => Future[_]): GrpcActionBuilder = {
    GrpcActionBuilder(name, ec)(session => f(session))
  }
}
