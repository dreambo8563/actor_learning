import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.routing.{FromConfig}

/**
  * Created by vincent on 9/5/2017.akka.tcp://backend@0.0.0.0:2551
  */

class EchoActor extends Actor with ActorLogging {
  def receive: Receive = {
    case message =>
      log.info("Received Message {} in Actor {}", message, self.path.name)
  }
}

object RouteTest extends App {
  val _system = ActorSystem("Router")
  val randomRouter = _system.actorOf(FromConfig.props(Props[EchoActor]), name = "router1")
  1 to 200 foreach {
    i =>
      //      println(i)
      randomRouter ! i
  }
}

