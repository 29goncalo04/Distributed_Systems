import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

// public class BancoMultiplasContas {
//     //Lock lock = new ReentrantLock();
//     private static class Account {
//         Lock lock2 = new ReentrantLock();
//         private int balance;
//         Account (int balance) { this.balance = balance; }
//         int balance () {
//             lock2.lock();
//             try{
//                 return balance;
//             } finally{
//                 lock2.unlock();
//             }
//         }
//         boolean deposit (int value) {
//             lock2.lock();
//             try{
//                 balance += value;
//                 return true;
//             } finally{
//                 lock2.unlock();
//             }
//         }
//         boolean withdraw (int value) {
//             lock2.lock();
//             try{
//                 if (value > balance)
//                     return false;
//                 balance -= value;
//                 return true;
//             } finally{
//                 lock2.unlock();
//             }
//         }
//     }
//     private final int slots;
//     private Account[] av;
//     public BancoMultiplasContas (int n) {
//         slots=n;
//         av=new Account[slots];
//         for (int i=0; i<slots; i++)
//             av[i]=new Account(0);
//     }
//     public int balance (int id) {
//         // lock.lock();
//         // try{
//         //     if (id < 0 || id >= slots)
//         //         return 0;
//         //     return av[id].balance();
//         // } finally{
//         //     lock.unlock();
//         // }
//         if (id < 0 || id >= slots) return 0;
//         return av[id].balance();
//     }
//     public boolean deposit (int id, int value) {
//         //lock.lock();
//         //try{
//         //    if (id < 0 || id >= slots)
//         //        return false;
//         //    return av[id].deposit(value);
//         //} finally{
//         //    lock.unlock();
//         //}
//         if (id < 0 || id >= slots) return false;
//         return av[id].deposit(value);
//     }
//     public boolean withdraw (int id, int value) {
//         // lock.lock();
//         // try{
//         //     if (id < 0 || id >= slots)
//         //         return false;
//         //     return av[id].withdraw(value);
//         // } finally{
//         //     lock.unlock();
//         // }
//         if (id < 0 || id >= slots) return false;
//         return av[id].withdraw(value);
//     }
//     public boolean transfer (int from, int to, int value) {
//         // lock.lock();
//         // try{
//         //     if(withdraw(from, value)) return deposit(to, value);
//         //     return false;
//         // } finally{
//         //     lock.unlock();
//         // }
//         int menor = Math.min(from, to);
//         int maior = Math.max(from, to);
//         av[menor].lock2.lock();
//         av[maior].lock2.lock();
//         try{
//             if(withdraw(from, value)) return deposit(to, value);
//             return false;
//         } finally{
//             av[maior].lock2.unlock();
//             av[menor].lock2.unlock();
//         }
//     }
//     public int totalBalance () {
//         // lock.lock();
//         // try{
//         //     int res = 0;
//         //     for(int i = 0; i<slots; i++){
//         //         res += balance(i);
//         //     }
//         //     return res;
//         // } finally{
//         //     lock.unlock();
//         // }
//         for(int i = 0; i<slots; i++){
//             av[i].lock2.lock();
//         }
//         try{
//             int res = 0;
//             for(int i = 0; i<slots; i++){
//                 res += balance(i);
//             }
//             return res;
//         } finally{
//             for(int i = 0; i<slots; i++){
//                 av[i].lock2.unlock();
//             }
//         }
//     }
// }









public class BancoMultiplasContas {

    private static class Account {
        ReentrantLock lConta = new ReentrantLock();
        private int balance;
        Account (int balance) { this.balance = balance; }
        int balance () { return balance; }
        boolean deposit (int value) {
            balance += value;
            return true;
        }
        boolean withdraw (int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
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
        av[id].lConta.lock();
        try{
            if (id < 0 || id >= slots)
                return 0;
            return av[id].balance();
        } finally{
            av[id].lConta.unlock();
        }
    }

    // Deposit
    public boolean deposit (int id, int value) {
        av[id].lConta.lock();
        try{
            if (id < 0 || id >= slots)
                return false;
            return av[id].deposit(value);
        } finally{
            av[id].lConta.unlock();
        }
    }

    // Withdraw; fails if no such account or insufficient balance
    public boolean withdraw (int id, int value) {
        av[id].lConta.lock();
        try{
            if (id < 0 || id >= slots)
                return false;
            return av[id].withdraw(value);
        } finally{
            av[id].lConta.unlock();
        }
    }

    // Transfer
    public boolean transfer (int from, int to, int value) {
        int menor = Math.min(from, to);
        int maior = Math.max(from, to);
        av[menor].lConta.lock();
        av[maior].lConta.lock();
        try{
            return withdraw(from, value) && deposit(to, value);
        } finally{
            av[maior].lConta.unlock(); 
            av[menor].lConta.unlock();
        }
    }

    // TotalBalance
    public int totalBalance () {
        ArrayList<Account> contas = new ArrayList<Account>();
        for(int i = 0; i<slots; i++){
            av[i].lConta.lock();
            contas.add(av[i]);
        }
        int res = 0;
        for(int i = 0; i<slots; i++){
            res+=balance(i);
            av[i].lConta.unlock();
        }
        return res;
    }
}