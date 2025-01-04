import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Agreement {
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    int nThreads;
    int max = 0;
    Agreement (int N) {
        nThreads = N;
    }
    int propose(int choice){
        lock.lock();
        try{
            nThreads--;
            if(choice>max) max = choice;
            if(nThreads>0){
                while(nThreads>0) condition.await();
            }
            else condition.signalAll();
            return max;
        } catch (Exception e){return -1;}
        finally{
            lock.unlock();
        }
    }
}