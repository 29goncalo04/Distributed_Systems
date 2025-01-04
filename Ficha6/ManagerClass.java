import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// public class ManagerImpl implements Manager{
//     private Lock l = new ReentrantLock();
//     private Condition cond = l.newCondition();
//     private List<String> currentPlayers = new ArrayList<>();
//     private int maxMin;

//     private List<String> join(String name, int minPlayers) throws InterruptedException{
//         l.lock();
//         try{
//             List<String> players = currentPlayers;
//             players.add(name);
//             maxMin = Math.max(maxMin, minPlayers);
//             if(players.size() == maxMin){
//                 currentPlayers = new ArrayList<>();
//                 maxMin = 0;
//                 cond.signalAll();
//             }
//             else{
//                 while(players == currentPlayers){
//                     cond.await();
//                 }
//             }
//             return players;
//         }
//         finally{
//             l.unlock();
//         }
//     }
// }






public class ManagerClass implements Manager{
    ReentrantLock mLock = new ReentrantLock();
    Condition cond = mLock.newCondition();
    List<String> currentPlayers = new ArrayList<String>();
    int minJogadores;
    public Raid join(String name, int minPlayers){
        mLock.lock();
        try{
            List<String> players = currentPlayers;
            players.add(name);
            minJogadores = Math.max(minJogadores, minPlayers);
            while (players.size() < minJogadores) cond.await();
            currentPlayers = new ArrayList<String>();
            minJogadores = 0;
            cond.signalAll();
            return new Raid(players);
        } finally{
            mLock.unlock();
        }
    }
}