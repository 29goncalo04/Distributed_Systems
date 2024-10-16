import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Barreira {

    private int ncorredores;
    private ReentrantLock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();


    public Barreira(int n) {
        this.ncorredores = n;
    }

    public void cheguei() {
        this.lock.lock();
        try {
            this.ncorredores = this.ncorredores-1;
            if (this.ncorredores > 0) {
                while (this.ncorredores > 0) {
                    this.cond.await();                    
                }
            }
            else {
                this.cond.signalAll();
            }
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            this.lock.unlock();
        }
    }

    public int corredores() {
        this.lock.lock();
        try {
            return this.ncorredores;
        }
        finally {
            this.lock.unlock();
        }
    }
}