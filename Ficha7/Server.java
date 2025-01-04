import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import static java.util.Arrays.asList;

// class ContactManager {
//     private HashMap<String, Contact> contacts = new HashMap<>();

//     public void update(Contact c) {
//         contacts.put(c.name(), c);
//     }

//     public ContactList getContacts() { 
//        ContactList contactos = new ContactList();
//        for(Contact c : contacts.values()) contactos.add(c);
//        return contactos;
//     }
// }

// class ServerWorker implements Runnable {
//     private Socket socket;
//     private ContactManager manager;

//     public ServerWorker(Socket socket, ContactManager manager) {
//         this.socket = socket;
//         this.manager = manager;
//     }

//     public void run() {
//         try{
//             DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
//             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//             while(true){
//                 try{
//                     manager.update(Contact.deserialize(in));
//                     manager.getContacts().serialize(out);
//                     out.flush();
//                 } catch (EOFException e){
//                     break;
//                 }
//             }
//             socket.shutdownOutput();
//             socket.shutdownInput();
//             socket.close();
//         } catch(IOException e){}
//     }
// }



// public class Server {

//     public static void main (String[] args) throws IOException {
//         ServerSocket serverSocket = new ServerSocket(12345);
//         ContactManager manager = new ContactManager();
//         // example pre-population
//         manager.update(new Contact("John", 20, 253123321, null, asList("john@mail.com")));
//         manager.update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
//         manager.update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));

//         while (true) {
//             Socket socket = serverSocket.accept();
//             Thread worker = new Thread(new ServerWorker(socket, manager));
//             worker.start();
//         }
//     }

// }






class ContactManager {
    private HashMap<String, Contact> contacts = new HashMap<>();

    public void update(Contact c) {
        contacts.put(c.name(), c);
    }

    public ContactList getContacts() {
        ContactList contactos = new ContactList();
        for(Contact c : contacts.values()) contactos.add(c); 
        return contactos;
    }
}

class ServerWorker implements Runnable {
    private Socket socket;
    private ContactManager manager;

    public ServerWorker(Socket socket, ContactManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    public void run() {
        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            while(true){
                try{
                    manager.update(Contact.deserialize(in));
                    manager.getContacts().serialize(out);
                    out.flush();
                } catch (Exception e){break;}
            }
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        } catch(Exception e){}
    }
}



public class Server {

    public static void main (String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        ContactManager manager = new ContactManager();
        // example pre-population
        manager.update(new Contact("John", 20, 253123321, null, asList("john@mail.com")));
        manager.update(new Contact("Alice", 30, 253987654, "CompanyInc.", asList("alice.personal@mail.com", "alice.business@mail.com")));
        manager.update(new Contact("Bob", 40, 253123456, "Comp.Ld", asList("bob@mail.com", "bob.work@mail.com")));

        while (true) {
            Socket socket = serverSocket.accept();
            Thread worker = new Thread(new ServerWorker(socket, manager));
            worker.start();
        }
    }

}