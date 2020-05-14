import java.io.*;
import java.util.function.*;

public class Messenger {
    public static void main(String args[]) {
        RecieveThread recieve = new RecieveThread(6066, new Consume<String>());

        System.out.println("Server at IP: " + recieve.getSocketIpAddress());
        //recieve.getServerSocket().getInetAddress());
        System.out.println("Listening on port: " + recieve.getSocketPort());
        //recieve.getServerSocket().getLocalPort());

        recieve.start();

        
        SendThread send = new SendThread("localhost", 6066);

        try {
            send.send( "First message" );
            send.send( "Second Message");
            send.send( "Third Message" );
            send.send("/close"); // Closes the server thread
        } catch (IOException e) { e.printStackTrace(); } 
    }
}

class Consume <T> implements Consumer<T>{
    public void accept(T data) {
        System.out.println("Received: "+data);
    }
}