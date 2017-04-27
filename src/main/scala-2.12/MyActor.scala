import akka.actor.{Actor, Props}
import akka.event.Logging

object MyActor {

  case class Greeting(from: String)

  case object Goodbye

  def props: Props = Props(new MyActor())
}

class MyActor extends Actor {
  val log = Logging(context.system, this)

  import MyActor._

  override def preStart(): Unit = {
    println(s"${self} preStart")
  }

  override def postStop(): Unit = {
    println(s"${self} postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println(s"${self} preRestart")
    context.children foreach { child ⇒
      context.unwatch(child)
      context.stop(child)
    }
    println("ready to invoke postStop")
    postStop()
  }

  override def postRestart(reason: Throwable): Unit = {
    println(s"${self} postRestart")
    println(s"ready to preStart")
    preStart()
  }

  def receive = {
    case Greeting(greeter) =>
      log.info(s"I was greeted by $greeter.")
    case Goodbye => log.info("Someone said goodbye to me.")
    case x => println(x)
  }
}