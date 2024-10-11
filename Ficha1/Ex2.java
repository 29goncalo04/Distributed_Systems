import java.util.ArrayList;

public class Ex2 {
    public static void main(String[] args){
        
        int n = 10;
        Bank2 bank = new Bank2();
        ArrayList<Thread> list = new ArrayList<>();

        for(int i = 0; i<n; i++){
            Thread t = new Thread(new Deposit(bank));
            t.start();
            list.add(t);
        }

        for(Thread t : list){
            try {
                t.join();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(bank.balance());
    }
}

class Deposit implements Runnable{

    Bank2 bank;
    
    public Deposit(Bank2 bank){
        this.bank = bank;
    }
    
    public void run(){
        int V = 100;
        long I = 1000;
        for(int i = 0; i<I; i++){
            bank.deposit(V);
        }
    }
}