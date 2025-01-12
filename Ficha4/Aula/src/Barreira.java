import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// public class Barreira {

//     private int nthreads;
//     private ReentrantLock lock = new ReentrantLock();
//     private Condition cond = lock.newCondition();


//     public Barreira(int n) {
//         this.nthreads = n;
//     }
    

//     public void await() {
//         lock.lock();
//         try {
//             this.nthreads --;
//             if (this.nthreads > 0) {
//                 while (this.nthreads > 0) {
//                     cond.await();                    
//                 }
//             }
//             else {
//                 cond.signalAll();
//             }
//         }
//         catch (InterruptedException e) {
//             throw new RuntimeException(e);
//         }
//         finally {
//             lock.unlock();
//         }
//     }
// }

public class Barreira{
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    int nThreads;
    int count = 0;
    int uso = 0;
    Barreira(int N){
        nThreads = N;
    }
    public void await() throws InterruptedException{
        lock.lock();
        try{
            int usoAtual = uso;
            count++;
            if(count == nThreads){
                uso++;
                count = 0;
                condition.signalAll();
            }
            else{
                while(usoAtual == uso) condition.await();
            }
        } finally{
            lock.unlock();
        }
    }
}