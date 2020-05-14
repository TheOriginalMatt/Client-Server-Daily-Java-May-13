# Client Server
## Daily Java Project for: 5/13/2020

### Description:
A simple program to send messages back and forth like a messenger

### Daily Java Projects
I'm creating a small Java project each day to help me learn Java. These aren't rigorously tested, but instead exist just so I can get a better understanding of some portion of Java.

### Points of Interest

+ I still have more to learn about how threading works, like the inner workings and design ideas behind `Thread.join()`
+ Using an implementation of the `Consumer` class as passing in a function as an argument is an interesting way of handling it. I haven't experimented with lambda functions yet, so those might still be an easier way of solving the problem. 
+ I'm able to now go from an empty file to creating and calling a thread while needing little to no documentation, which is pretty neat.
+ I'm not sure how to document getters and setters, so far I've done it the same as every other function, but it just gets redundant when the only line in a function is `return this.variable;`.

### Using `Client Server`

```
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

```

### Documentation
#### `RecieveThread`
+ Constructors
    + `RecieveThread(int port)`

    + `RecieveThread(int port, Consumer consumer)`
+ Methods
    + `public void run()`
        + Waits for a client to connect and to receive messages. Stops when it receives `/close`
    + `public void start()`
        + Creates the thread and calls run()
    + `public void join() throws InterruptedException`
        + Joins this thread
    + `close()`
        + Closes all the sockets opened by RecieveThread
    + `public void stopListening() throws InterruptedException, IOException`
        + Stops the server, joins the thread, and closes the ports
    + `public void setConsumer(Consumer consumer)`
        + Runs Consumer implementation for each new message
    + `public Consumer getConsumer()`
        + Getter function for Consumer function
    + `public InetAddress getSocketIpAddress() throws NullPointerException`
        + Returns the IpAddress of the socket
    + `public int getSocketPort() throws NullPointerException`
        + Returns the port that the socket is listening to

#### `SendThread`
+ Constructors
    + `SendThread(String ip, int port)`
+ Methods
    + `public void send(String msg) throws IOException, NullPointerException`
        + Sends a message via the DataOutputStream object. Send `/close` to end a connection
    + `public InetAddress getSocketIpAddress() throws NullPointerException`
        + Returns a reference to the IP Address of the server connected to by the socket
    + `public int getSocketPort() throws NullPointerException`
        + Returns a reference to the port number connected to by the Socket