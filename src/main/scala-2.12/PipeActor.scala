import akka.actor.{Actor, ActorLogging, Props}

/**
  * Created by vincent on 27/4/2017.
  */
object PipeActor {

  def props: Props = Props(new PipeActor())
}

class PipeActor extends Actor with ActorLogging {

  def receive = {
    case Result(x: Int) =>
      println(s"get response $x")
  }

}
