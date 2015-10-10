import java.net.InetSocketAddress

import com.twitter.finagle.Service
import com.twitter.finagle.builder.ServerBuilder
import com.twitter.finagle.httpx.{Http, Request, Response, Status}
import com.twitter.util._
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import ma.thele.PostponingTimer

/**
 * A sample Finagle-based HTTP-server using
 * a special postponing timer to re-schedule Future.sleeps.
 *
 * Running n requests simultaneously will
 * increase the average response time by n factor.
 *
 * @author Evgeniy Muravev
 * @since 10.10.2015
 */
object ConfinServer extends App with LazyLogging {
  private val conf = ConfigFactory.load()

  private val host = conf.getString("host")

  private val port = conf.getInt("port")

  private val avgResponseMillis = conf.getInt("avgTimeMillis")

  private val avgResponseDuration = Duration.fromMilliseconds(avgResponseMillis)

  // The postponing timer delays the execution of a scheduled task
  // whether other tasks have not completed yet
  implicit val timer = new PostponingTimer(avgResponseMillis)

  val rootService = new Service[Request, Response] {
    def apply(req: Request) = {
      import com.twitter.util.Future._

      req.uri match {
        case "/foo/bar" =>
          sleep(avgResponseDuration) before value(Response(Status.Ok))
        case other =>
          logger.warn(s"Requested unavailable resource: `$other`")
          value(Response(Status.NotFound))
      }
    }
  }

  {
    logger.debug("Starting the server...")

    val address = new InetSocketAddress(host, port)
    val server = ServerBuilder()
      .codec(Http())
      .bindTo(address)
      .name("ConfinSampleServer")
      .build(rootService)

    Await.ready(server)
  }
}


