import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerWorker implements Runnable{
    Socket socket;

    public ServerWorker(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            int sum = 0;
            int n = 0;
            String line;
            while ((line = in.readLine()) != null) {
                try {
                    sum += Integer.parseInt(line);
                    n++;
                }
                catch (NumberFormatException e) {}
                out.println(sum);
                out.flush();
            }
            if (n<1) {
                n = 1;
            }
            out.println ((double) sum/n);
            out.flush();
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } 
        catch (IOException e){}
    }
}