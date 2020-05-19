import java.io.*;
import java.util.function.*;
import ClientServer.*;

public class Messenger {
    public static void main(String args[]) {
        RecieveThread recieve = new RecieveThread(6066, new Consume());

        System.out.println("Server at IP: " + recieve.getSocketIpAddress());
        //recieve.getServerSocket().getInetAddress());
        System.out.println("Listening on port: " + recieve.getSocketPort());
        //recieve.getServerSocket().getLocalPort());

        recieve.start();

        
        SendThread send = new SendThread("192.168.1.13", 6066);

        try {
            send.send( "First message" );
            send.send( "Second Message");
            send.send( "Third Message" );
            send.send("/close"); // Closes the server thread
        } catch (IOException e) { e.printStackTrace(); } 
    }
}

class Consume implements Consumer<String>{
    public void accept(String data) {
        System.out.println("Received: "+data);
    }
}