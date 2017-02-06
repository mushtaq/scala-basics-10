package actors

import java.util.concurrent.TimeUnit

import asynchrony.Config


class AccountManager {

  private var currentBalance = 0.0

  def deposit(amount: Double): Unit =  {
    Config.threadpool.schedule(
      new Runnable {
        override def run() = synchronized {
          currentBalance += amount
        }
      },
      1000,
      TimeUnit.MILLISECONDS
    )
  }

  def balance: Double = currentBalance
}

object App4 extends App {

  private val accountManager = new AccountManager

  (1 to 200).par.foreach { x =>
    accountManager.deposit(10)
  }

  Thread.sleep(3000)

  Config.threadpool.shutdownNow()
  println(accountManager.balance)
}