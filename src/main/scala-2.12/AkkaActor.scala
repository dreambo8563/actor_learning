/**
  * Created by vincent on 27/4/2017.
  */


import akka.actor.{ActorSystem, DeadLetter, Props}
import akka.util.Timeout
import akka.pattern.{ask, pipe}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import akka.pattern.gracefulStop


import scala.concurrent.Await

final case class Result(x: Int)

case object Request


object AkkaActor extends App {

  import DemoActor._

  println("Hello, world!")
  val system = ActorSystem("mySystem")

  import system.dispatcher

  val demoActor = system.actorOf(props(42), "demoActor")
  val pipeActor = system.actorOf(PipeActor.props, "pipeActor")

  system.eventStream.subscribe(demoActor, classOf[DeadLetter])
  println(s"demoActor created !")
  //  demoActor ! 88
  //  println(s"ready to kill demoActor's child")

  val managerActor = system.actorOf(Props[Manager], "managerActor")
  try {
      system.scheduler.scheduleOnce(5000 milliseconds) {
        val stopped: Future[Boolean] = gracefulStop(managerActor, 5 seconds, Manager.Shutdown)
        Await.result(stopped, 6 seconds)
      }
    // the actor has been stopped
  } catch {
    // the actor wasn't stopped within 5 seconds
    case e: akka.pattern.AskTimeoutException => println(e.getMessage)
  }

  //  implicit val timeout = Timeout(5 seconds) // needed for `?` below
  //
  //  val f: Future[Result] =
  //    for {
  //      x <- ask(demoActor, Request).mapTo[Int] // call pattern directly
  //    } yield Result(x)
  //
  //  f pipeTo pipeActor
  //
  //  f onComplete {
  //    case Success(x) => println(s"success $x")
  //    case Failure(e) => println(s"fail ${e.getMessage} in callback of future")
  //  }
  //
  //  system.scheduler.scheduleOnce(5000 milliseconds) {
  //    demoActor ! 1000
  //  }
}
