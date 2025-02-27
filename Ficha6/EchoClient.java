import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));

            String userInput;
            while (!(userInput = systemIn.readLine()).equals("Exit")) {
                out.println(userInput);
                out.flush();

                String response = in.readLine();
                System.out.println("The current sum is: " + response);
            }

            socket.shutdownOutput();
            String response = in.readLine();
            System.out.println("The average is " + response);
            socket.shutdownInput();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


