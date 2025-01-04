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


public class Barreira {
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    int nThreads;
    Barreira (int n) {
        nThreads = n;
    }
    void await() {
        lock.lock();
        try{
            nThreads--;
            if(nThreads>0){
                while(nThreads>0) condition.await();
            }
            else condition.signalAll();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        finally{
            lock.unlock();
        }
    }
}