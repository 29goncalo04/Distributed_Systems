import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

// public class TaggedConnection implements AutoCloseable {

//     public static class Frame {
//         public final int tag;
//         public final byte[] data;
//         public Frame(int tag, byte[] data) {
//             this.tag = tag; 
//             this.data = data;
//         }
//     }

//     DataInputStream in;
//     DataOutputStream out;
//     ReentrantLock rLock = new ReentrantLock();
//     ReentrantLock wLock = new ReentrantLock();

//     public TaggedConnection(Socket socket) throws IOException {
//         this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//         this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//     }

//     //public void send(Frame frame) throws IOException {
//     //    send(frame.tag, frame.data);
//     //}

//     public void send(int tag, byte[] data) throws IOException {
//         wLock.lock();
//         try{
//             out.writeInt(tag);
//             out.writeInt(data.length);
//             out.write(data);
//             out.flush();
//         } finally{
//             wLock.unlock();
//         }
//     }
    
//     public Frame receive() throws IOException {
//         rLock.lock();
//         try{
//             int tag = in.readInt();
//             int tamanho = in.readInt();
//             byte[] data = new byte[tamanho];
//             in.readFully(data);
//             return new Frame(tag, data);
//         } finally{
//             rLock.unlock();
//         }
//     }
    
//     public void close() throws IOException {
//         in.close();
//         out.close();
//     }
// }



public class TaggedConnection implements AutoCloseable {
    public static class Frame {
        public final int tag;
        public final byte[] data;
        public Frame(int tag, byte[] data) { this.tag = tag; this.data = data; }
    }
    ReentrantLock rLock = new ReentrantLock();
    ReentrantLock wLock = new ReentrantLock();
    Socket socket;
    DataInputStream in;
    DataOutputStream out;
    public TaggedConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }
    // public void send(Frame frame) throws IOException {

    // }
    public void send(int tag, byte[] data) throws IOException {
        wLock.lock();
        try{
            out.writeInt(tag);
            out.writeInt(data.length);
            out.write(data);
            out.flush();
        } finally{
            wLock.unlock();
        }
    }
    public Frame receive() throws IOException {
        rLock.lock();
        try{
            int tag = in.readInt();
            int tamanho = in.readInt();
            byte[] data = new byte[tamanho];
            in.readFully(data);
            return new Frame(tag, data);
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