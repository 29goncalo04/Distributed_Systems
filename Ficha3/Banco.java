import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.*;

// public class Banco {
//     private static class Account {
//         Lock conta = new ReentrantLock();
//         private int balance;
//         Account(int balance) { 
//             this.balance = balance; 
//         }
//         int balance() { 
//             conta.lock();
//             try{
//                 return balance;
//             } finally{
//                 conta.unlock();
//             }
//         }
//         boolean deposit(int value) {
//             conta.lock();
//             try{
//                 balance += value;
//                 return true;
//             } finally{
//                 conta.unlock();
//             }
//         }
//         boolean withdraw(int value) {
//             conta.lock();
//             try{
//                 if (value > balance)
//                 return false;
//                 balance -= value;
//                 return true;
//             } finally{
//                 conta.unlock();
//             }
//         }
//     }
//     private Map<Integer, Account> map = new HashMap<Integer, Account>();
//     private int nextId = 0;
//     Lock banco = new ReentrantLock();
//     public int createAccount(int balance) {
//         Account c = new Account(balance);
//         banco.lock();   //ao contraŕio da ficha 2 aqui é preciso fazer lock a todo o banco porque pode haver outras threads a inserir ou remover elementos do map
//         try{
//             int id = nextId;
//             nextId += 1;
//             map.put(id, c);
//             return id;
//         } finally{
//             banco.unlock();
//         }
//     }
//     public int closeAccount(int id) {
//         banco.lock();
//         try{
//             Account c = map.remove(id);
//             if (c == null)
//                 return 0;
//             return c.balance();   //pode estar depois do unlock
//         } finally{
//             banco.unlock();
//         }
//     }
//     public int balance(int id) {
//         banco.lock();
//         try{
//             Account c = map.get(id);
//             if (c == null)
//                 return 0;
//             return c.balance();
//         } finally{
//             banco.unlock();
//         }
//     }
//     public boolean deposit(int id, int value) {
//         banco.lock();
//         try{
//             Account c = map.get(id);
//             if (c == null)
//                 return false;
//             return c.deposit(value);
//         } finally{
//             banco.unlock();
//         }
//     }
//     public boolean withdraw(int id, int value) {
//         banco.lock();
//         try{
//             Account c = map.get(id);
//             if (c == null)
//                 return false;
//             return c.withdraw(value);
//         } finally{
//             banco.unlock();
//         }
//     }
//     public boolean transfer(int from, int to, int value) {
//         banco.lock();
//         //try{
//             Account cfrom, cto;
//             cfrom = map.get(from);
//             cto = map.get(to);
//             if (cfrom == null || cto ==  null){
//                 banco.unlock();
//                 return false;
//             }
//             if (from < to){
//                 cfrom.conta.lock();
//                 try{
//                     cto.conta.lock();
//                     banco.unlock();
//                     try{
//                         return cfrom.withdraw(value) && cto.deposit(value);
//                     } finally{
//                         cto.conta.unlock();
//                     }
//                 } finally{
//                     cfrom.conta.unlock();
//                 }
//             }
//             else{
//                 cto.conta.lock();
//                 try{
//                     cfrom.conta.lock();
//                     banco.unlock();
//                     try{
//                         return cfrom.withdraw(value) && cto.deposit(value);
//                     } finally{
//                         cfrom.conta.unlock();
//                     }
//                 } finally{
//                     cto.conta.unlock();
//                 }
//             }
//         //} finally{
//         //   banco.unlock();
//         //}
//     }
//     public int totalBalance(int[] ids) {
//         int total = 0;
//         banco.lock();
//         Account[] contas = new Account[ids.length];
//         for (int i = 0; i<ids.length; i++) {
//             contas[i] = map.get(ids[i]);
//             if (contas[i] == null){
//                 banco.unlock();
//                 return 0;
//             }
//         }
//         for(int i = 0; i<ids.length; i++){
//             contas[i].conta.lock();
//         }
//         banco.unlock();
//         for(int i = 0; i<ids.length; i++){
//             total += contas[i].balance();
//             contas[i].conta.unlock();
//     }
//         return total;
//     }
// }






// public class Banco {
//     Lock lockBanco = new ReentrantLock();
//     private static class Account {
//         Lock lockConta = new ReentrantLock();
//         private int balance;
//         Account(int balance) { this.balance = balance; }
//         int balance() {
//             //lockConta.lock();
//             //try{
//                 return balance; 
//             //} finally{
//             //    lockConta.unlock();
//             //}
//         }
//         boolean deposit(int value) {
//             //lockConta.lock();
//             //try{
//                 balance += value;
//                 return true;
//             //} finally{
//             //    lockConta.unlock();
//             //}
//         }
//         boolean withdraw(int value) {
//             //lockConta.lock();
//             //try{
//                 if (value > balance)
//                     return false;
//                 balance -= value;
//                 return true;
//             //} finally{
//             //    lockConta.unlock();
//             //}
//         }
//     }
//     private Map<Integer, Account> map = new HashMap<Integer, Account>();
//     private int nextId = 0;
//     public int createAccount(int balance) {
//         lockBanco.lock();
//         try{
//             Account c = new Account(balance);
//             int id = nextId;
//             nextId += 1;
//             map.put(id, c);
//             return id;
//         } finally{
//             lockBanco.unlock();
//         }
//     }
//     public int closeAccount(int id) {
//         lockBanco.lock();
//         Account c = map.remove(id);
//         if (c == null){
//             lockBanco.unlock();
//             return 0;
//         }
//         c.lockConta.lock();
//         lockBanco.unlock();
//         try{
//             return c.balance();
//         } finally{
//             c.lockConta.unlock();
//         }
//     }
//     public int balance(int id) {
//         lockBanco.lock();
//         Account c = map.get(id);
//         if (c == null){
//             lockBanco.unlock();
//             return 0;
//         }
//         c.lockConta.lock();
//         lockBanco.unlock();
//         int res = c.balance();
//         c.lockConta.unlock();
//         return res;
//     }
//     public boolean deposit(int id, int value) {
//         lockBanco.lock();
//         Account c = map.get(id);
//         if (c == null){
//             lockBanco.unlock();
//             return false;
//         }
//         c.lockConta.lock();
//         lockBanco.unlock();
//         boolean res = c.deposit(value);
//         c.lockConta.unlock();
//         return res;
//     }
//     public boolean withdraw(int id, int value) {
//         lockBanco.lock();
//         Account c = map.get(id);
//         if (c == null){
//             lockBanco.unlock();
//             return false;
//         }
//         c.lockConta.lock();
//         lockBanco.unlock();
//         boolean res = c.withdraw(value);
//         c.lockConta.unlock();
//         return res;
//     }
//     public boolean transfer(int from, int to, int value) {
//         lockBanco.lock();
//         Account cfrom, cto;
//         cfrom = map.get(from);
//         cto = map.get(to);
//         if (cfrom == null || cto ==  null){
//             lockBanco.unlock();
//             return false;
//         }
//         if (from<to){
//             cfrom.lockConta.lock();
//             cto.lockConta.lock();
//         }
//         else{
//             cto.lockConta.lock();
//             cfrom.lockConta.lock();
//         }
//         lockBanco.unlock();
//         boolean res = cfrom.withdraw(value) && cto.deposit(value);
//         if (from<to){
//             cto.lockConta.unlock();
//             cfrom.lockConta.unlock();
//         }
//         else{
//             cfrom.lockConta.unlock();
//             cto.lockConta.unlock();
//         }
//         return res;
//     }
//     public int totalBalance(int[] ids) {
//         int total = 0;
//         ArrayList<Account> contas = new ArrayList<Account>();
//         lockBanco.lock();
//         for (int i : ids) {
//             Account c = map.get(i);
//             if (c == null){
//                 lockBanco.unlock();
//                 for(Account a : contas) a.lockConta.unlock();
//                 return 0;
//             }
//             c.lockConta.lock();
//             contas.add(c);
//         }
//         lockBanco.unlock();
//         for (Account a : contas){
//             total += a.balance();
//             a.lockConta.unlock();
//         }
//         return total;
//     }
// }








class Banco {
    ReadWriteLock bLock = new ReentrantReadWriteLock();
    private static class Account {
        ReadWriteLock cLock = new ReentrantReadWriteLock();
        private int balance;
        Account(int balance) { this.balance = balance; }
        int balance() { return balance; }
        boolean deposit(int value) {
            balance += value;
            return true;
        }
        boolean withdraw(int value) {
            if (value > balance)
                return false;
            balance -= value;
            return true;
        }
    }

    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;

    // create account and return account id
    public int createAccount(int balance) {
        bLock.writeLock().lock();
        try{
            Account c = new Account(balance);
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        } finally{
            bLock.writeLock().unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        bLock.writeLock().lock();
        Account c = map.remove(id);
        if (c == null){
            bLock.writeLock().unlock();
            return 0;
        }
        c.cLock.readLock().lock();
        bLock.writeLock().unlock();
        int res = c.balance();
        c.cLock.readLock().unlock();
        return res;
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        bLock.readLock().lock();
        Account c = map.get(id);
        if (c == null){
            bLock.readLock().unlock();
            return 0;
        }
        c.cLock.readLock().lock();
        bLock.readLock().unlock();
        int res = c.balance();
        c.cLock.readLock().unlock();
        return res;
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        bLock.readLock().lock();
        Account c = map.get(id);
        if (c == null){
            bLock.readLock().unlock();
            return false;
        }
        c.cLock.writeLock().lock();
        bLock.readLock().unlock();
        boolean res = c.deposit(value);
        c.cLock.writeLock().unlock();
        return res;
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        bLock.readLock().lock();
        Account c = map.get(id);
        if (c == null){
            bLock.readLock().unlock();
            return false;
        }
        c.cLock.writeLock().lock();
        bLock.readLock().unlock();
        boolean res = c.withdraw(value);
        c.cLock.writeLock().unlock();
        return res;
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        bLock.readLock().lock();
        Account cfrom, cto;
        cfrom = map.get(from);
        cto = map.get(to);
        if (cfrom == null || cto ==  null){
            bLock.readLock().unlock();
            return false;
        }
        if(from<to){
            cfrom.cLock.writeLock().lock();
            cto.cLock.writeLock().lock();
        }
        else{
            cto.cLock.writeLock().lock();
            cfrom.cLock.writeLock().lock();
        }
        bLock.readLock().unlock();
        boolean res = cfrom.withdraw(value) && cto.deposit(value);
        if(from<to){
            cto.cLock.writeLock().unlock();
            cfrom.cLock.writeLock().unlock();
        }
        else{
            cfrom.cLock.writeLock().unlock();
            cto.cLock.writeLock().unlock();
        }
        return res;
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        bLock.readLock().lock();
        int total = 0;
        ArrayList<Account> contas = new ArrayList<Account>();
        for (int i : ids) {
            Account c = map.get(i);
            if (c == null){
                for(Account a : contas) a.cLock.readLock().unlock();
                bLock.readLock().unlock();
                return 0;
            }
            c.cLock.readLock().lock();
            contas.add(c);
        }
        bLock.readLock().unlock();
        for(Account a : contas){
            total += a.balance();
            a.cLock.readLock().unlock();
        }
        return total;
    }

}