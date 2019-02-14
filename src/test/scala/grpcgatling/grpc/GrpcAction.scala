package grpcgatling.grpc

import io.gatling.commons.stats.OK
import io.gatling.commons.util.Clock
import io.gatling.core.action.Action
import io.gatling.core.session.{Expression, Session}
import io.gatling.core.stats.StatsEngine
import io.gatling.core.util.NameGen
import io.grpc.StatusRuntimeException

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class GrpcAction(clock: Clock,
                      statsEngine: StatsEngine,
                      requestName: Expression[String],
                      next: Action,
                      send: Expression[Future[_]],
                      ec: ExecutionContext)
    extends Action
    with NameGen {
  override def name: String = genName("grpcRequest")

  override def execute(session: Session): Unit = {
    val start = clock.nowMillis

    val x = for {
      name <- requestName(session)
      res <- send(session)
    } yield {
      res.onComplete {
        case Success(_) =>
          statsEngine.logResponse(session,
                                  name,
                                  start,
                                  clock.nowMillis,
                                  OK,
                                  None,
                                  None)
          next.!(session)

        case Failure(e) =>
          val code = e match {
            case s: StatusRuntimeException => Some(s.getStatus.toString)
            case _                         => Some("UNKNOWN")
          }
          statsEngine.logResponse(session,
                                  name,
                                  start,
                                  clock.nowMillis,
                                  OK,
                                  code,
                                  Some(e.getMessage))
          next.!(session)
      }(ec)
    }

    x.onFailure { msg =>
      statsEngine.reportUnbuildableRequest(session, "", msg)
      next.!(session)
    }
  }
}
