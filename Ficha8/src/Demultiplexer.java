// import java.io.IOException;
// import java.util.concurrent.locks.ReentrantLock;

// public class Demultiplexer implements AutoCloseable {

//     TaggedConnection conn;
//     ReentrantLock lock = new ReentrantLock();

//     public Demultiplexer(TaggedConnection conn) {
//         this.conn = conn;
//     }

//     public void start() {

//     }

//     public void send(Frame frame) throws IOException {

//     }
    
//     public void send(int tag, byte[] data) throws IOException {

//     }
    
//     public byte[] receive(int tag) throws IOException, InterruptedException {

//     }
    
//     public void close() throws IOException {
        
//     }
// }