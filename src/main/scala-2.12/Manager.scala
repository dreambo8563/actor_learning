import akka.actor.{Actor, ActorLogging, DeadLetter, PoisonPill, Props, Terminated}

import scala.concurrent.duration._

/**
  * Created by vincent on 2/5/2017.
  */
object Manager {

  case object Shutdown

}

class Manager extends Actor with ActorLogging {

  import Manager._

  //  import PipeActor._
  val worker = context.watch(context.actorOf(Props[PipeActor], "worker"))
  implicit val dispatch = context.system.dispatcher
  val cancellable =
    context.system.scheduler.schedule(
      0 milliseconds,
      10 milliseconds,
      worker,
      "test")

  def receive = {
    case "job" => worker ! "crunch"
    case Shutdown =>
      worker ! PoisonPill
      context become shuttingDown
  }

  def shuttingDown: Receive = {
    case "job" => sender() ! "service unavailable, shutting down"
    case x => println(x)
    case Terminated(`worker`) =>
      printf("workers are already stopped")
      context stop self
    case DeadLetter(msg, from, to) =>
      log.info("get DEAD LETTER")
  }

}
