import java.io.*;
import java.util.function.*;

public class Messenger {
    public static void main(String args[]) {
        RecieveThread recieve = new RecieveThread(6066, new Consume<String>());

        System.out.println("Server at IP: " + recieve.getServerSocket().getInetAddress());
        System.out.println("Listening on port: " + recieve.getServerSocket().getLocalPort());

        recieve.start();

        
        SendThread send = new SendThread("localhost", 6066);

        send.start();

        // try {
        //     recieve.stopListening();
        // } 
        // catch(IOException e)          { e.printStackTrace(); }
        // catch(InterruptedException e) { e.printStackTrace(); }
    }


}

class Consume <T> implements Consumer<T>{
    public void accept(T data) {
        System.out.println("Received: "+data);
    }
}