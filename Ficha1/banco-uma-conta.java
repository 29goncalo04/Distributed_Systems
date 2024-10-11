import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Bank2 {

  Lock lock = new ReentrantLock();

  private static class Account {
    private int balance;

    Account(int balance) {
      this.balance = balance;
    }

    int balance() {
      return balance;
    }

    boolean deposit(int value) {
      balance += value;
      return true;
    }
  }

  // Our single account, for now
  private Account savings = new Account(0);

  // Account balance
  public int balance() {
    return savings.balance();
  }

  // Deposit
  boolean deposit(int value) {
    lock.lock();
    try {
      return savings.deposit(value);
    } finally {
      lock.unlock();
    }
  }
}