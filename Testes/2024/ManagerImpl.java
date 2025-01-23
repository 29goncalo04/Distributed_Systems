import java.util.concurrent.locks.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.*;

interface Manager{
    Trip permission(int size) throws InterruptedException;
}
interface Trip{
    int dockId();
    void waitDisembark() throws InterruptedException;
    void finishedDisembark();
    void depart();
}

public class ManagerImpl implements Manager {
    private Lock lock = new ReentrantLock();
    private Condition[] conditions = new Condition[7]; // Condições para cada classe de navios
    private boolean[] docas = new boolean[28]; // Estado das docas
    private Queue<TripImpl>[] emEspera = new Queue[7]; // Fila de navios esperando

    public ManagerImpl() {
        for (int i = 0; i < 7; i++) {
            conditions[i] = lock.newCondition();
            emEspera[i] = new ArrayDeque<>();
        }
    }

    public Trip permission(int size) throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                for (int i = size*4; i < 28; i++) {
                    if (!docas[i]) { // Se a doca estiver livre
                        docas[i] = true;
                        TripImpl trip = new TripImpl(i, size);
                        return trip; // Se encontrou uma doca, o navio pode atracar
                    }
                }

                // Se não encontrou uma doca, coloca o navio na fila
                TripImpl trip = new TripImpl(-1, size); // -1 significa que ainda não tem dock atribuído
                emEspera[size].add(trip);
                conditions[size].await(); // Espera até que uma doca apropriada esteja disponível
            }
        } finally {
            lock.unlock();
        }
    }

        class TripImpl implements Trip {
            int dockId;
            int size;
            Lock lockT = new ReentrantLock();
            Condition condT = lockT.newCondition();
            boolean completed = false;

            TripImpl(int dockId, int size) {
                this.dockId = dockId;
                this.size = size;
            }

            public int dockId() {
                lockT.lock();
                try{
                    return dockId;
                } finally{
                    lockT.unlock();
                }
            }

            public void waitDisembark() throws InterruptedException {
                lockT.lock();
                try {
                    while (!completed) condT.await(); // Espera o desembarque ser concluído
                } finally {
                    lockT.unlock();
                }
            }

            public void finishedDisembark() {
                lockT.lock();
                try {
                    completed = true;
                    condT.signal(); // Avisa que o desembarque foi concluído
                } finally {
                    lockT.unlock();
                }
            }

            public void depart() {
                lock.lock();
                try {
                    docas[dockId] = false; // Libera a doca
                    for(int i = 6; i>=0; i--){
                        if(!emEspera[i].isEmpty() && dockId>=i*4){
                            TripImpl nextTrip = emEspera[i].poll();
                            conditions[i].signal();
                            break;
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
}