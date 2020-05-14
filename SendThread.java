import java.net.*;
import java.io.*;

class SendThread {

    Socket socket = null;
    InetAddress ipAddress;
    int port;

    DataOutputStream outStream;

    /*
     * SendThread()
     * 
     * Description: Constructor
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
// SOCKET METHODS
//
//
//////

    /*
     * CreateSocket()
     * 
     * Description: Generates a Socket connecting to the given IP Address and
     *      port
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
    private void createSocket() throws UnknownHostException, IOException {
        this.socket = new Socket(this.getIpAddress(), this.getPort());
    }

    /*
     * createDataOutputStream()
     * 
     * Description: Creates a DataOutputStream object via the given socket
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
    private void createDataOutputStream() throws IOException, NullPointerException {
        if (this.getSocket() != null) {
            this.outStream = new DataOutputStream(this.getSocket().getOutputStream());
        } else {
            throw new NullPointerException("Must first create a socket.");
        }
    }

    /*
     * send()
     * 
     * Description: Sends a message via the DataOutputStream object
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

    /*
     * setIpAddress()
     * 
     * Description: Creates an InetAddress object from a String containing an 
     *      IPv4 IP Address
     * 
     * Arguments:
     *   String - address - String version of an IPv4 IP Address
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    private void setIpAddress(String address) throws UnknownHostException  {
        this.ipAddress = InetAddress.getByName(address);
    }

    /*
     * getIpAddress
     * 
     * Description: Getter function for this.ipAddress
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   InetAddress - this.ipAddress
     * 
     * Notes:
     *   N/A
     *
     */
    private InetAddress getIpAddress() {
        return this.ipAddress;
    }

    /*
     * getSocketIpAddress
     * 
     * Description: Returns a reference to the IP Address of the server 
     *      connected to by the socket
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   InetAddress - IP Address of the server
     * 
     * Notes:
     *   N/A
     *
     */
    public InetAddress getSocketIpAddress() throws NullPointerException { 
        if (this.getSocket() != null) {
            return this.getSocket().getInetAddress(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }

    /*
     * setPort()
     * 
     * Description: Validates and sets the port number of the server
     * 
     * Arguments:
     *   int - port - port number (0 <= port <= 65535)
     * 
     * Returns:
     *   N/A 
     * 
     * Notes:
     *   N/A
     *
     */
    private void setPort(int port) {
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
     * Description: Returns the port number to contact the server at
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   int - reference to the port number to contact the server at
     * 
     * Notes:
     *   N/A
     *
     */
    private int getPort() {
        return this.port;
    }

    /*
     * getSocketPort()
     * 
     * Description: Returns the port number connected to by the Socket
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   int - the port number connected to by the Socket
     * 
     * Notes:
     *   N/A
     *
     */
    public int getSocketPort() throws NullPointerException { 
        if (this.getSocket() != null) {
            return this.getSocket().getPort(); 
        } else {
            throw new NullPointerException("Socket is not defined");
        }
    }

    /*
     * getSocket()
     * 
     * Description: Returns a reference to the Socket used by this object
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   Socket - reference to the Socket used by this object
     * 
     * Notes:
     *   N/A
     *
     */
    private Socket getSocket() { return this.socket; }

    /*
     * DataOutputStream()
     * 
     * Description: Returns a reference to the DataOutputStream used by
     *      this object
     * 
     * Arguments:
     *   N/A
     * 
     * Returns:
     *   DataOutputStream -  reference to the DataOutputStream used by this 
     *      object
     * 
     * Notes:
     *   N/A
     *
     */
    private DataOutputStream getDataOutputStream() {
        return this.outStream;
    }
}