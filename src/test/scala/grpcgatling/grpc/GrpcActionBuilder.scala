package grpcgatling.grpc

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.session.Expression
import io.gatling.core.structure.ScenarioContext

import scala.util.Try

case class GrpcActionBuilder(requestName: Expression[String])(send: Expression[Try[Unit]]) extends ActionBuilder { self =>
  override def build(ctx: ScenarioContext, next: Action): Action = {
    GrpcAction(ctx.coreComponents.clock, ctx.coreComponents.statsEngine, requestName, next, send)
  }
}
