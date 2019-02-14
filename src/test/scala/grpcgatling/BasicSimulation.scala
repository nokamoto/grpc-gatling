package grpcgatling

import grpcgatling.grpc.GrpcActionBuilder
import io.gatling.commons.validation._
import io.gatling.core.Predef._
import io.gatling.core.session.Expression

import scala.util.Try

abstract class BasicSimulation extends Simulation {
  protected[this] def grpc(name: Expression[String])(
      f: Session => Unit): GrpcActionBuilder = {
    GrpcActionBuilder(name)(session => Try(f(session)).success)
  }
}
