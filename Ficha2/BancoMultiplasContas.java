import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BancoMultiplasContas {

    //Lock lock2 = new ReentrantLock();
    
    private static class Account {
        Lock lock = new ReentrantLock();
        private int balance;
        Account (int balance) { this.balance = balance; }
        int balance (){
            return balance;
        }
        boolean deposit (int value) {
            lock.lock();
            try{
                balance += value;
                return true;
            }
            finally{
               lock.unlock();
            }
        }
        boolean withdraw (int value) {
            lock.lock();
            try{
                if (value > balance)
                return false;
                balance -= value;
                return true;
            }
            finally{
               lock.unlock();
            }
        }
    }

    // Bank slots and vector of accounts
    private final int slots;
    private Account[] av;

    public BancoMultiplasContas (int n) {
        slots=n;
        av=new Account[slots];
        for (int i=0; i<slots; i++)
            av[i]=new Account(0);
    }


    // Account balance
    public int balance (int id) {
        if (id < 0 || id >= slots)
            return 0;
        //lock.lock();
        //try{
            return av[id].balance();
        //} finally{
        //    lock.unlock();
        //}
    }

    // Deposit
    public boolean deposit (int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        //lock.lock();
        //try{
            return av[id].deposit(value);
        //} finally{
        //    lock.unlock();
        //}
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw (int id, int value) {
        if (id < 0 || id >= slots)
            return false;
        //lock.lock();
        //try{
            return av[id].withdraw(value);
        //} finally{
        //    lock.unlock();
        //}
    }

    // Transfer
    public boolean transfer (int from, int to, int value) {
        if(from<to){
            av[from].lock.lock();
            try{
                av[to].lock.lock();
                try{
                    if(withdraw(from, value)) return deposit(to, value);
                    return false;
                } finally{
                    av[to].lock.unlock();
                }
            } finally{
                av[from].lock.unlock();
            }
        }
        else{
            av[to].lock.lock();
            try{
                av[from].lock.lock();
                try{
                    if(withdraw(from, value)) return deposit(to, value);
                    return false;
                } finally{
                    av[from].lock.unlock();
                }
            } finally{
                av[to].lock.unlock();
            }
        }
        //lock.lock();
        //try{
            // if(withdraw(from, value)) return deposit(to, value);
            // return false;
        //} finally{
        //    lock.unlock();
        //}
    }

    // TotalBalance
    public int totalBalance () {
        //lock.lock();
        //try{
            for(int i = 0; i<slots; i++){
                av[i].lock.lock();
            }
            int result = 0;
            for(int i = 0; i<slots; i++){
                result+=balance(i);
            }
            for(int i = 0; i<slots; i++){
                av[i].lock.unlock();
            }
            return result;
        //} finally{
        //    lock.unlock();
        //}
    }
}