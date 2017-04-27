/**
  * Created by vincent on 27/4/2017.
  */

import akka.actor.{ActorSystem, DeadLetter}

object AkkaActor extends App {

  import DemoActor._

  println("Hello, world!")
  val system = ActorSystem("mySystem")
  val demoActor = system.actorOf(props(42), "demoActor")
  system.eventStream.subscribe(demoActor, classOf[DeadLetter])
  println(s"demoActor created !")
  demoActor ! 88
  println(s"ready to kill demoActor's child")
  demoActor ! "kill"
}
