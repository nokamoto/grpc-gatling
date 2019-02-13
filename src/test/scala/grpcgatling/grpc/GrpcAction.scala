package grpcgatling.grpc

import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.Clock
import io.gatling.core.action.Action
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen

import scala.util.Try

case class GrpcAction(clock: Clock, statsEngine: StatsEngine, requestName: Expression[String], next: Action, send: Expression[Try[Unit]]) extends Action with NameGen {
  override def name: String = genName("grpcRequest")

  override def execute(session: Session): Unit = {
    val start = clock.nowMillis

    val x = for {
      name <- requestName(session)
      res <- send(session)
    } yield {
      val end = clock.nowMillis
      val status = res.fold(_ => KO, _ => OK)
      val code = res.fold(e => throw new NotImplementedError("todo"), _ => None)
      val msg = res.fold(e => Some(e.getMessage), _ => None)
      statsEngine.logResponse(session, name, start, clock.nowMillis, status, code, msg)
    }

    x.onFailure(msg => statsEngine.reportUnbuildableRequest(session, "", msg))

    next.!(session)
  }
}
