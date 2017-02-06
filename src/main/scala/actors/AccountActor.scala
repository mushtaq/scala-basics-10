package actors

import actors.AccountActor.{CurrentBalance, Deposit, DepositAccepted, GetBalance}
import akka.Done
import akka.actor.{Actor, ActorRef, ActorSystem, Props, Stash}
import akka.pattern.ask
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import asynchrony.FutureExtensions.RichFuture

import scala.concurrent.duration.DurationLong

class AccountActor extends Actor with Stash {

  var currentBalance = 0.0
  val scheduler = context.system.scheduler

  import context.dispatcher

  override def receive: Receive = {
    case Deposit(amount)                  =>
      scheduler.scheduleOnce(1000.millis, self, DepositAccepted(amount, sender()))
      context.become(awaitingResponse)
      stash()
    case GetBalance                       =>
      sender() ! CurrentBalance(currentBalance)
  }

  def awaitingResponse: Receive = {
    case DepositAccepted(amount, replyTo) =>
      currentBalance += amount
      replyTo ! Done
      context.unbecome()
      unstashAll()
    //val future = someActor.square(10) => Future[Int]
    //future.foreach(x => self ! Deposit(amount))
  }
}

object AccountActor {
  def props() = Props(new AccountActor)

  case class Deposit(amount: Double)

  case class DepositAccepted(amount: Double, replyTo: ActorRef)

  case class CurrentBalance(amount: Double)

  case object GetBalance

}

object App5 extends App {
  private val actorSystem = ActorSystem("TW")
  private val actorRef: ActorRef = actorSystem.actorOf(AccountActor.props())
  implicit val timeout = Timeout(20.seconds)

  import async.Async._
  import asynchrony.Config._

  val done = Source(1 to 200)
    .mapAsync(200)(x => actorRef ? Deposit(10))
    .runWith(Sink.ignore)

  val result = async {
    await(done)
    await(actorRef ? GetBalance)
  }.show()

  println(result)

  actorSystem.terminate()
}
