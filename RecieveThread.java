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

    /*
     * RecieveThread()
     * 
     * Description: Constructor that doesn't set the Consumer
     * 
     * Arguments:
     *   int - port - Port number to listen at
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */ 
    RecieveThread(int port) {
        this.setPort(port);
        try {
            this.createServerSocket();
        } catch (IOException e) { e.printStackTrace(); }
    }

    /*
     * RecieveThread()
     * 
     * Description: Constructor that accepts the method to run each time it receives a message
     * 
     * Arguments:
     *   int - port - Port number to listen at
     *   Consumer - consumer - Implementation of the Consumer interface, runs 
     *      consumer.accept() each time the server receives a message
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */ 
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

    /*
     * run()
     * 
     * Description: Waits for a client to connect and to receive messages
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   Closes the sockets and stops the thread if it receives `/close`
     *
     */
    public void run() {
        while(!this.getStopListening()) {
            try {
                this.connectSocket();
                this.createDataInputStream();
                break;

            } catch(IOException e) { e.printStackTrace(); }
            
        }

        while (!this.getStopListening()) {
            try {
                String message = this.getDataInputStream().readUTF();
                this.getConsumer().accept(message);
                if (message.equals("/close")) {
                    try {
                        this.stopListening();
                    } catch (InterruptedException e){ e.printStackTrace(); }
                }
            } catch(IOException e) { e.printStackTrace(); }        
        }
    }

    /*
     * start()
     * 
     * Description: Creates the thread and calls run()
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    public void start() {
        if (this.t == null) {
            this.t = new Thread(this, "RecieveThread");
            this.t.start();
        }
    }

    /*
     * join()
     * 
     * Description: Joins this thread
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
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

    /*
     * createServerSocket()
     * 
     * Description: Creates the server socket to listen to 
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    private void createServerSocket() throws IOException {
        if (this.getPort() != -1) {
            this.serverSocket = new ServerSocket(this.getPort());
        }
    }

    /*
     * close()
     * 
     * Description: Closes all the sockets opened by RecieveThread
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    public void close() throws IOException {
        this.getServerSocket().close();
        this.getSocketConnection().close();
    }

    /*
     * createDataInputStream()
     * 
     * Description: Creates the input stream to receive text from the client
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    private void createDataInputStream() throws IOException {
        if (this.getSocketConnection() != null) {
            this.inStream = new DataInputStream(this.getSocketConnection().getInputStream());
        }
    }

    /*
     * createSocket()
     * 
     * Description: Creates the socket to connect with the client
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
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

    /*
     * setPort()
     * 
     * Description: Validates and sets the port number
     * 
     * Arguments:
     *   int - port - the port number that will be watched, must be between 0 and 65535 inclusively
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    private void setPort(int port) throws IllegalArgumentException {
        if (port >= 0 && port <= 65535) {
            this.port = port;
        } else {
            throw new IllegalArgumentException(
                "Port number must be between 0 and 65535 inclusively"
            );
        }
    }

    /*
     * getPort()
     * 
     * Description: Getter function for this.port
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   int - port number
     * 
     * Notes:
     *   N/A
     *
     */
    private int getPort() {
        return this.port;
    }

    /*
     * getServerSocket()
     * 
     * Description: Getter function for this.serverSocket
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   ServerSocket - the ServerSocket being used
     * 
     * Notes:
     *   N/A
     *
     */
    private ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    /*
     * stopListening()
     * 
     * Description: Stops the server, joins the thread, and closes the ports
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    public void stopListening() throws InterruptedException, IOException {
        this.stopListening = true;
        this.close();
    }

    private boolean getStopListening() {
        return this.stopListening;
    }

    /*
     * getSocketConnection
     * 
     * Description: Getter function for this.socket
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   Socket - socket used by this object
     * 
     * Notes:
     *   N/A
     *
     */
    private Socket getSocketConnection() {
        return this.socket;
    }

    /*
     * getDataInputStream()
     * 
     * Description: Getter function for this.inStream
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   DataInputStream - DataInputStream used by this object 
     * 
     * Notes:
     *   N/A
     *
     */
    private DataInputStream getDataInputStream() {
        return this.inStream;
    }

    /*
     * setConsumer()
     * 
     * Description: Runs Consumer implementation for each new message
     * 
     * Arguments:
     *   Consumer<T> - Consumer implementation to run on each message
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    public void setConsumer(Consumer consumer) {
        this.onRead = consumer;
    }

    /*
     * getConsumer()
     * 
     * Description: Getter function for Consumer function
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   Consumer - current Consumer implementation
     * 
     * Notes:
     *   N/A
     *
     */
    public Consumer getConsumer() {
        if (this.onRead != null) {
            return this.onRead;
        } else {
            throw new NullPointerException(
                "Must define Consumer<T> before calling it."
            );
        }
    }

    /*
     * getSocketIpAddress()
     * 
     * Description: Returns the IpAddress of the socket
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   InetAddress - IpAddress of the socket
     * 
     * Notes:
     *   N/A
     *
     */
    public InetAddress getSocketIpAddress() throws NullPointerException { 
        if (this.getServerSocket() != null) {
            return this.getServerSocket().getInetAddress(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }

    /*
     * getSocketPort()
     * 
     * Description: Returns the port that the socket is listening to
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   int - Port number the server is listening to
     * 
     * Notes:
     *   N/A
     *
     */
    public int getSocketPort() throws NullPointerException { 
        if (this.getServerSocket() != null) {
            return this.getServerSocket().getLocalPort(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }
}