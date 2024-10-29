import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ManagerImpl implements Manager{
    private Lock l = new ReentrantLock();
    private Condition cond = l.newCondition();
    private List<String> currentPlayers = new ArrayList<>();
    private int maxMin;

    private List<String> join(String name, int minPlayers) throws InterruptedException{
        l.lock();
        try{
            List<String> players = currentPlayers;
            players.add(name);
            maxMin = Math.max(maxMin, minPlayers);
            if(players.size() == maxMin){
                currentPlayers = new ArrayList<>();
                maxMin = 0;
                cond.signalAll();
            }
            else{
                while(players == currentPlayers){
                    cond.await();
                }
            }
            return players;
        }
        finally{
            l.unlock();
        }
    }
}