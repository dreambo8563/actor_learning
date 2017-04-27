/**
  * Created by vincent on 27/4/2017.
  */

import akka.actor.{Actor, ActorIdentity, ActorLogging, DeadLetter, Identify, Props, Terminated}


object DemoActor {
  /**
    * Create Props for an actor of this type.
    *
    * @param magicNumber The magic number to be passed to this actor’s constructor.
    * @return a Props for creating this actor, which can then be further configured
    *         (e.g. calling `.withDispatcher()` on it)
    */
  def props(magicNumber: Int): Props = Props(new DemoActor(magicNumber))
}

class DemoActor(magicNumber: Int) extends Actor with ActorLogging {

  import MyActor._

  println(s"magicNumber: $magicNumber")
  val myActor = context.actorOf(MyActor.props, "myActor")
  val identifyId = 1
  println(s"create myActor: ${myActor.path}")
  context.actorSelection("myActor") ! Identify(identifyId)

  context.watch(myActor)
  println("watching myActor")


  def receive = {
    case ActorIdentity(`identifyId`, Some(ref)) =>
      println(s"get actorRef: $ref")
//      context.watch(ref)
    case ActorIdentity(`identifyId`, None) => println("not found the actorRef")
    case x: Int =>
      myActor ! Greeting(s"Vincent Guo with params $x")

    case "kill" =>
      context.stop(myActor);
      println("stopping myActor")
      myActor ! Greeting(s"after kill")
    case DeadLetter(msg, from, to) =>
      log.info("get DEAD LETTER")
      self ! msg
    case Terminated(`myActor`) => println(s"$myActor finished")
    case m => println(m)
  }

}