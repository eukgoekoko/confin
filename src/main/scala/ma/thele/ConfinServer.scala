import com.twitter.finagle.{Httpx, Service}
import com.twitter.finagle.httpx
import com.twitter.util.{Await, Future}
import com.typesafe.scalalogging.LazyLogging


object ConfinServer extends App with LazyLogging {
  val service = new Service[httpx.Request, httpx.Response] {
    def apply(req: httpx.Request): Future[httpx.Response] =
      Future.value(
        httpx.Response(req.version, httpx.Status.Ok)
      )
  }

  {
    logger.debug("Starting the server...")
    val server = Httpx.serve(":8080", service)
    Await.ready(server)
  }
}


