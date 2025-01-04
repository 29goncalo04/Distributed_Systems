import java.util.ArrayList;

// public class Ex2 {
//     public static void main(String[] args){
//         int n = 10;
//         Bank2 bank = new Bank2();
//         ArrayList<Thread> list = new ArrayList<>();
//         for(int i = 0; i<n; i++){
//             Thread t = new Thread(new Deposit(bank));
//             t.start();
//             list.add(t);
//         }
//         for(Thread t : list){
//             try {
//                 t.join();
//             } catch (Exception e) {
//                 throw new RuntimeException(e);
//             }
//         }
//         System.out.println(bank.balance());
//     }
// }

// class Deposit implements Runnable{
//     Bank2 bank;
//     public Deposit(Bank2 bank){
//         this.bank = bank;
//     }
//     public void run(){
//         int V = 100;
//         long I = 1000;
//         for(int i = 0; i<I; i++){
//             bank.deposit(V);
//         }
//     }
// }




// public class Ex2 {
//     public static void main(String[] args){
//         Bank bank = new Bank();
//         ArrayList<Thread> threads = new ArrayList<Thread>();
//         int n = 10;
//         for(int i = 0; i<n; i++){
//             Thread t = new Thread(new Deposit(bank));
//             threads.add(t);
//             t.start();
//         }
//         for(Thread t : threads){
//             try{
//                 t.join();
//             } catch (Exception e){}
//         }
//         System.out.println(bank.balance());
//     }
// }

// class Deposit implements Runnable{
//     Bank bank;
//     public Deposit(Bank banco){
//         this.bank = banco;
//     }
//     public void run(){
//         final int i = 1000;
//         final int v = 100;
//         for (int j = 0; j<i; j++){
//             bank.deposit(v);
//         }
//     }
// }




public class Ex2{
    public static void main(String[] args){
        int n = 10;
        Bank banco = new Bank();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for(int j = 0; j<n; j++){
            Thread t = new Thread(new Deposit(banco));
            threads.add(t);
            t.start();
        }
        for(Thread t : threads){
            try{
                t.join();
            } catch (Exception e){}
        }
        System.out.println(banco.balance());
    }
}


class Deposit implements Runnable{
    Bank banco;
    public Deposit(Bank banco){
        this.banco = banco;
    }
    public void run(){
        final int i = 1000;
        final int v = 100;
        for(int j = 0; j<i; j++){
            banco.deposit(v);
        }
    }
}