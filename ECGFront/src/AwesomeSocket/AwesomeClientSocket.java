package AwesomeSockets;

import java.io.*;
import java.net.Socket;

/**
 * Created by JiaHao on 22/2/15.
 */
public class AwesomeClientSocket {


    private final Socket serverSocket;
    private final InputStream clientInputStream;
    private final BufferedReader serverReader;
    private final PrintWriter serverWriter;
    private final OutputStream clientOutputStream;


    public boolean debugMode = true;

    public AwesomeClientSocket(String hostName, int port) throws IOException {

        printDebugMessages("Waiting for server...");
        this.serverSocket = new Socket(hostName, port);

        printDebugMessages("Server connected.");

        this.clientInputStream = this.serverSocket.getInputStream();

        this.serverReader = new BufferedReader(
                new InputStreamReader(
                        this.clientInputStream
                ));


        this.clientOutputStream = this.serverSocket.getOutputStream();
        this.serverWriter = new PrintWriter(this.clientOutputStream, true);


    }


    @Deprecated
    public BufferedReader getServerReader() throws IOException {

        return this.serverReader;

    }

    @Deprecated
    public PrintWriter getServerWriter() throws IOException {


        return this.serverWriter;
    }

    public InputStream getClientInputStream() {
        return this.clientInputStream;
    }

    public OutputStream getClientOutputStream() {
        return this.clientOutputStream;
    }


    /**
     * Sends a message to the server
     * @param message
     */
    public void sendMessageLine(String message) {

        PrintWriter writer = this.serverWriter;
        writer.println(message);
        writer.flush();

    }

    /**
     * Blocks and reads a message from the server
     * TODO add timeout for read call
     * @return
     * @throws java.io.IOException
     */
    public String readMessageLine() throws IOException {

        printDebugMessages("Reading message from server...");

        BufferedReader reader = this.serverReader;

        String message = reader.readLine();
        printDebugMessages("Message received from server.");
        return message;

    }


    public void sendByteArray(byte[] myByteArray) throws IOException {

//        if (len < 0)
//            throw new IllegalArgumentException("Negative length not allowed");
//        if (start < 0 || start >= myByteArray.length)
//            throw new IndexOutOfBoundsException("Out of bounds: " + start);
//        // Other checks if needed.

        // May be better to save the streams in the support class;
        // just like the socket variable.
        OutputStream out = this.getClientOutputStream();
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(myByteArray.length);
        if (myByteArray.length > 0) {
            dos.write(myByteArray, 0, myByteArray.length);
        }
    }

    public byte[] readByteArray() throws IOException {
        InputStream in = this.getClientInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }

        return data;
    }

    /**
     * Close the client
     * @throws java.io.IOException
     */
    public void closeClient() throws IOException {
        this.serverSocket.close();
        printDebugMessages("Client closed.");
    }

    /**
     * Helper function to only print debug messages if we are in debug mode
     * @param message
     */
    private void printDebugMessages(String message) {

        if (this.debugMode) {
            System.out.println(message);
        }
    }

    @Override
    public String toString() {
        return this.serverSocket.toString();
    }

    public static void main(String[] args) {

    }
}
