import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;


// public class FramedConnection implements AutoCloseable {
//     ReentrantLock rlock = new ReentrantLock();
//     ReentrantLock wlock = new ReentrantLock();
//     Socket socket;
//     DataInputStream in;
//     DataOutputStream out;

//     public FramedConnection(Socket socket) throws IOException {
//         this.socket = socket;
//         this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//         this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//     }

//     public void send(byte[] data) throws IOException {
//         wlock.lock();
//         try{
//             out.writeInt(data.length);
//             out.write(data);
//             out.flush();
//         } finally{
//             wlock.unlock();
//         }
//     }

//     public byte[] receive() throws IOException {
//         rlock.lock();
//         try{
//             int tamanho = in.readInt();
//             byte[] data = new byte[tamanho];
//             in.readFully(data);
//             return data;
//         } finally{
//             rlock.unlock();
//         }
//     }

//     public void close() throws IOException {
//         in.close();
//         out.close();
//     }
// }





public class FramedConnection implements AutoCloseable {
    ReentrantLock rLock = new ReentrantLock();
    ReentrantLock wLock = new ReentrantLock();
    DataInputStream in;
    DataOutputStream out;
    Socket socket;

    public FramedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void send(byte[] data) throws IOException {
        wLock.lock();
        try{
            out.writeInt(data.length);
            out.write(data);
            out.flush();
        } finally{
            wLock.unlock();
        }
    }

    public byte[] receive() throws IOException {
        rLock.lock();
        try{
            int tamanho = in.readInt();
            byte[] data = new byte[tamanho];
            in.readFully(data);
            return data;
        } finally{
            rLock.unlock();
        }
    }
    
    public void close() throws IOException {
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }
}