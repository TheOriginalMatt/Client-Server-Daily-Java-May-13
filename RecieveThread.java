import java.net.*;
import java.io.*;
import java.util.function.*;


class RecieveThread implements Runnable{
    private Thread t;
    private String threadName;

    private int port = -1;
    private ServerSocket serverSocket;
    private boolean stopListening = false;
    private Socket socket;

    private DataInputStream inStream;

    private Consumer onRead;


    RecieveThread(int port) {
        this.setPort(port);
        try {
            this.createServerSocket();
        } catch (IOException e) { e.printStackTrace(); }
    }

    RecieveThread(int port, Consumer consumer) {
        this.setPort(port);
        this.setConsumer(consumer);
        try {
            this.createServerSocket();
        } catch (IOException e) { e.printStackTrace(); }
    }

//////
//
//
// THREAD METHODS
//
//
//////

    public void run() {
        while(true) {
        //     System.out.println("Listening");
        //     try {
        //         this.connectSocket();
        //         this.getConsumer().accept(this.getDataInputStream());

        //     } catch(IOException e) { e.printStackTrace(); }
            
        // }
            try {
                System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
                
                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());
                
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress()  + "\nGoodbye!");
                server.close();
            
            } catch (SocketTimeoutException s) {
                System.out.println("Socket timed out!");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
    }

    public void start() {
        if (this.t == null) {
            this.t = new Thread(this, "RecieveThread");
            this.t.start();
        }
    }

    public void join() throws InterruptedException {
        this.t.join();
    }

//////
//
//
// SOCKET METHODS
//
//
//////

    private void createServerSocket() throws IOException {
        if (this.getPort() != -1) {
            this.serverSocket = new ServerSocket(this.getPort());
        }
    }

    public void close() throws IOException {
        this.getServerSocket().close();
        this.getSocketConnection().close();
    }

    private void createDataInputStream() throws IOException {
        if (this.getSocketConnection() != null) {
            this.inStream = new DataInputStream(this.getSocketConnection().getInputStream());
        }
    }

    private void connectSocket() throws IOException {
        if (this.getServerSocket() != null) {
            this.socket = this.getServerSocket().accept();
        }
    }


//////
//
//
// GETTER & SETTER METHODS
//
//
//////
    
    private void setThreadName(String name) {
        this.threadName = new String(name);
    }


    public String getThreadName() {
        return this.threadName;
    }    

    private void setPort(int port) throws IllegalArgumentException {
        if (port >= 0 && port <= 65535) {
            this.port = port;
        } else {
            throw new IllegalArgumentException(
                "Port number must be between 0 and 65535 inclusively"
            );
        }
    }

    public int getPort() {
        return this.port;
    }

    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    public void stopListening() throws InterruptedException, IOException {
        this.stopListening = true;
        this.join();
        this.close();
    }

    private Socket getSocketConnection() {
        return this.socket;
    }

    private DataInputStream getDataInputStream() {
        return this.inStream;
    }

    public void setConsumer(Consumer consumer) {
        this.onRead = consumer;
    }

    public Consumer getConsumer() {
        if (this.onRead != null) {
            return this.onRead;
        } else {
            throw new IllegalArgumentException(
                "Must define Consumer<T> before calling it."
            );
        }
    }
}