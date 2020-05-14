import java.net.*;
import java.io.*;

class SendThread implements Runnable{
    Thread t;

    Socket socket = null;
    InetAddress ipAddress;
    int port;

    DataOutputStream outStream;

    SendThread(String ip, int port) {  
        try {
            this.setIpAddress(ip);
        } catch (UnknownHostException e) { e.printStackTrace(); }

        this.setPort(port);

        try {
            this.createSocket();
        } catch (IOException e) { e.printStackTrace(); }

        try {
            this.createDataOutputStream();
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
        System.out.println("Connecting to IP Address: "+this.getSocketIpAddress());
        System.out.println("Connecting on Port: "+this.getSocketPort());

        try {
            this.send("HELLO WORLD!!!");
        } catch(IOException e) { e.printStackTrace(); }

    }

    public void start() {
       if (this.t == null) {
            this.t = new Thread(this, "SendThread");
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

    private void createSocket() throws UnknownHostException, IOException {
        this.socket = new Socket(this.getIpAddress(), this.getPort());
    }

    private void createDataOutputStream() throws IOException, NullPointerException {
        if (this.getSocket() != null) {
            this.outStream = new DataOutputStream(this.getSocket().getOutputStream());
        } else {
            throw new NullPointerException("Must first create a socket.");
        }
    }

    public void send(String msg) throws IOException, NullPointerException {

        if (this.getDataOutputStream() != null) {
            this.getDataOutputStream().writeUTF(msg);
            System.out.println("Sent: "+msg);
        } else {
            throw new NullPointerException(
                "Must first define DataOutputStream."
            );
        }
        
    }

//////
//
//
// GETTER & SETTER METHODS
//
//
//////

    private void setIpAddress(String address) throws UnknownHostException  {
        this.ipAddress = InetAddress.getByName(address);
    }

    private InetAddress getIpAddress() {
        return this.ipAddress;
    }

    public InetAddress getSocketIpAddress() throws NullPointerException { 
        if (this.getSocket() != null) {
            return this.getSocket().getInetAddress(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }

    private void setPort(int port) {
        if (port >= 0 && port <= 65535) {
            this.port = port;
        } else {
            throw new IllegalArgumentException(
                "Port number must be between 0 and 65535 inclusively"
            );
        }
    }

    private int getPort() {
        return this.port;
    }

    public int getSocketPort() throws NullPointerException { 
        if (this.getSocket() != null) {
            return this.getSocket().getPort(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }

    private Socket getSocket() { return this.socket; }

    private DataOutputStream getDataOutputStream() {
        return this.outStream;
    }

    public boolean isConnected() {
        return this.getSocket().isBound();
    }

    public void waitForConnection() {        
        while (true) {    
            if (this.getSocket() != null) { 
                break; 
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException f) { f.printStackTrace(); }
        } 
    }
}