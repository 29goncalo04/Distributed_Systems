import java.util.*;
import java.util.concurrent.locks.*;

class Bank {

    private static class Account {
        ReadWriteLock conta = new ReentrantReadWriteLock();
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
    ReadWriteLock banco = new ReentrantReadWriteLock();


    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        banco.writeLock().lock();
        try{
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        } finally{
            banco.writeLock().unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        banco.writeLock().lock();
        try{
            Account c = map.remove(id);
            if (c == null) return 0;
            c.conta.readLock().lock();
            try{
                return c.balance();
            } finally{
                c.conta.readLock().unlock();
            }
        } finally{
            banco.writeLock().unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        banco.readLock().lock();
        try{
            Account c = map.get(id);
            if (c == null) return 0;
            c.conta.readLock().lock();
            try{
                return c.balance();
            } finally{
                c.conta.readLock().unlock();
            }
        } finally{
            banco.readLock().unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        banco.readLock().lock();
        try{
            Account c = map.get(id);
            if (c == null) return false;
            c.conta.writeLock().lock();
            try{
                return c.deposit(value);
            } finally{
                c.conta.writeLock().unlock();
            }
        } finally{
            banco.readLock().unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        banco.readLock().lock();
        try{
            Account c = map.get(id);
            if (c == null) return false;
            c.conta.writeLock().lock();
            try{
                return c.withdraw(value);
            } finally{
                c.conta.writeLock().unlock();
            }
        } finally{
            banco.readLock().unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        Account cfrom, cto;
        banco.readLock().lock();
        //try{
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto ==  null){
                banco.readLock().unlock();
                return false;
            }
            Account primeiro, segundo;
            if(from < to){
                primeiro = cfrom;
                segundo = cto;
            }
            else{
                primeiro = cto;
                segundo = cfrom;
            }
            primeiro.conta.writeLock().lock();
            try{
                segundo.conta.writeLock().lock();
                banco.readLock().unlock();
                try{
                    return cfrom.withdraw(value) && cto.deposit(value);
                } finally{
                    segundo.conta.writeLock().unlock();
                }
            } finally{
                primeiro.conta.writeLock().unlock();
            }
        //} finally{
        //    banco.readLock().unlock();
        //}
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        banco.readLock().lock();
        Account[] contas = new Account[ids.length];
        for(int i = 0; i<ids.length; i++){
            contas[i] = map.get(ids[i]);
            if(contas[i]==null){
                banco.readLock().unlock();
                return 0;
            }
        }
        for(int i = 0; i<ids.length; i++){
            contas[i].conta.readLock().lock();
        }
        banco.readLock().unlock();
        for(int i = 0; i<ids.length; i++){
            total += contas[i].balance();
            contas[i].conta.readLock().unlock();
        }
        return total;
    }
}