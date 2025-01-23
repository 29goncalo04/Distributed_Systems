import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MuseumManagerImpl implements MuseumManager {
    public static final int N = 10;
    private Lock lock = new ReentrantLock();
    private Condition[] conds = new Condition[N];
    private Queue<String>[] espera = new Queue[N];
    private int bilheteAtual = 0;
    private Map<String, Integer> usos = new HashMap<>();
    private Map<Integer, Integer> capacidades = new HashMap<>();  //??????????????
    private Map <Integer, Integer> pessoas = new HashMap<>();

    public MuseumManagerImpl(){
        for(int i = 0; i<N; i++){
            conds[i] = lock.newCondition();
            espera[i] = new ArrayDeque<>();
        }
    }
    
    public String buyTicket(int uses){
        lock.lock();
        try{
            String res = Integer.toString(bilheteAtual);
            bilheteAtual++;
            usos.put(res, uses);
            return res;
        } finally{
            lock.unlock();
        }
    }

    public int enterGallery(int galleryId, String ticketId) throws InterruptedException{
        lock.lock();
        try{
            if(usos.get(ticketId)==0) return 0;
            if(pessoas.get(galleryId-1) < capacidades.get(galleryId-1)){
                pessoas.put(galleryId-1, pessoas.get(galleryId-1)+1);
                usos.put(ticketId, usos.get(ticketId)-1);
                return galleryId;
            }
            else{
                espera[galleryId-1].add(ticketId);
                while(pessoas.get(galleryId-1) >= capacidades.get(galleryId-1)) conds[galleryId-1].await();
                pessoas.put(galleryId-1, pessoas.get(galleryId-1)+1);
                usos.put(ticketId, usos.get(ticketId)-1);
                return galleryId;
            }
        } finally{
            lock.unlock();
        }
    }

    public void exitGallery(int galleryId, String ticketId){
        lock.lock();
        try{
            pessoas.put(galleryId-1, pessoas.get(galleryId-1)-1);
            if(!espera[galleryId-1].isEmpty()){
                String ticket = espera[galleryId-1].poll();
                conds[galleryId-1].signal();
            }
        } finally{
            lock.unlock();
        }
    }

    public Map<Integer, Integer> peopleWaitingPerGallery(){
        lock.lock();
        try{
            Map<Integer, Integer> res = new HashMap<>();
            for(int i = 0; i<N; i++){
                int aux = espera[i].size();
                res.put(i+1, aux);
            }
            return res;
        } finally{
            lock.unlock();
        }
    }
}




