import akka.actor.Actor

/**
  * Created by vincent on 2/5/2017.
  */
class HotSwapActor extends Actor {

  import context._

  def angry: Receive = {
    case "foo" => sender() ! "I am already angry?"
    case "bar" => become(happy)
    case "ex" => throw new Exception("exception here")
    case _ => println("angry")
  }

  def happy: Receive = {
    case "bar" => sender() ! "I am already happy :-)"
    case "foo" => become(angry)
    case _ => println("happy")
  }

  def receive = {
    case "foo" => become(angry)
    case "bar" => become(happy)
    case _ => println("default")
  }
}