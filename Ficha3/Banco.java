import java.util.*;
import java.util.concurrent.locks.*;

public class Banco {
    
    private static class Account {
        Lock conta = new ReentrantLock();
        private int balance;
        Account(int balance) { 
            this.balance = balance; 
        }
        int balance() { 
            conta.lock();
            try{
                return balance;
            } finally{
                conta.unlock();
            }
        }
        boolean deposit(int value) {
            conta.lock();
            try{
                balance += value;
                return true;
            } finally{
                conta.unlock();
            }
        }
        boolean withdraw(int value) {
            conta.lock();
            try{
                if (value > balance)
                return false;
                balance -= value;
                return true;
            } finally{
                conta.unlock();
            }
        }
    }
    
    private Map<Integer, Account> map = new HashMap<Integer, Account>();
    private int nextId = 0;
    Lock banco = new ReentrantLock();
    
    // create account and return account id
    public int createAccount(int balance) {
        Account c = new Account(balance);
        banco.lock();   //ao contraŕio da ficha 2 aqui é preciso fazer lock a todo o banco porque pode haver outras threads a inserir ou remover elementos do map
        try{
            int id = nextId;
            nextId += 1;
            map.put(id, c);
            return id;
        } finally{
            banco.unlock();
        }
    }

    // close account and return balance, or 0 if no such account
    public int closeAccount(int id) {
        banco.lock();
        try{
            Account c = map.remove(id);
            if (c == null)
                return 0;
            return c.balance();   //pode estar depois do unlock
        } finally{
            banco.unlock();
        }
    }

    // account balance; 0 if no such account
    public int balance(int id) {
        banco.lock();
        try{
            Account c = map.get(id);
            if (c == null)
                return 0;
            return c.balance();
        } finally{
            banco.unlock();
        }
    }

    // deposit; fails if no such account
    public boolean deposit(int id, int value) {
        banco.lock();
        try{
            Account c = map.get(id);
            if (c == null)
                return false;
            return c.deposit(value);
        } finally{
            banco.unlock();
        }
    }

    // withdraw; fails if no such account or insufficient balance
    public boolean withdraw(int id, int value) {
        banco.lock();
        try{
            Account c = map.get(id);
            if (c == null)
                return false;
            return c.withdraw(value);
        } finally{
            banco.unlock();
        }
    }

    // transfer value between accounts;
    // fails if either account does not exist or insufficient balance
    public boolean transfer(int from, int to, int value) {
        banco.lock();
        //try{
            Account cfrom, cto;
            cfrom = map.get(from);
            cto = map.get(to);
            if (cfrom == null || cto ==  null){
                banco.unlock();
                return false;
            }
            if (from < to){
                cfrom.conta.lock();
                try{
                    cto.conta.lock();
                    banco.unlock();
                    try{
                        return cfrom.withdraw(value) && cto.deposit(value);
                    } finally{
                        cto.conta.unlock();
                    }
                } finally{
                    cfrom.conta.unlock();
                }
            }
            else{
                cto.conta.lock();
                try{
                    cfrom.conta.lock();
                    banco.unlock();
                    try{
                        return cfrom.withdraw(value) && cto.deposit(value);
                    } finally{
                        cfrom.conta.unlock();
                    }
                } finally{
                    cto.conta.unlock();
                }
            }
        //} finally{
        //   banco.unlock();
        //}
    }

    // sum of balances in set of accounts; 0 if some does not exist
    public int totalBalance(int[] ids) {
        int total = 0;
        banco.lock();
        Account[] contas = new Account[ids.length];
        for (int i = 0; i<ids.length; i++) {
            contas[i] = map.get(ids[i]);
            if (contas[i] == null){
                banco.unlock();
                return 0;
            }
        }
        for(int i = 0; i<ids.length; i++){
            contas[i].conta.lock();
        }
        banco.unlock();
        for(int i = 0; i<ids.length; i++){
            total += contas[i].balance();
            contas[i].conta.unlock();
    }
        return total;
    }

}