package AwesomeSockets;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;



/**
 * Created by JiaHao on 22/2/15.
 */
public class AwesomeServerSocket {
//
//    public final BufferedReader clientReader;
//    public final BufferedReader clientWriter;
//

    private ArrayList<Socket> clients = null;
    private ArrayList<OutputStream> serverOutputStreams = null;
    private ArrayList<InputStream> serverInputStreams = null;

    private ArrayList<PrintWriter> clientWriters = null;
    private ArrayList<BufferedReader> clientReaders = null;
    private ArrayList<Thread> clientListenerThreads = null;

    public boolean debugMode = true;

    // counter to track next client
    private int counter = 0;


    private final ServerSocket serverSocket;

    public AwesomeServerSocket(int port) throws IOException {

        this.serverSocket = new ServerSocket(port);
    }

    /**
     * Sets the timeout for the server to wait when accepting a client
     * @param milliseconds
     * @throws java.net.SocketException
     */
    public void setServerSoTimeout(int milliseconds) throws SocketException {
        this.serverSocket.setSoTimeout(milliseconds);
    }

    /**
     * BLOCKS until it accepts a client or until the serverSOTimeout if set.
     * Argument soTimeOut is for the individual clients that have been accepted, to set timeout on
     * their bufferedReader.readLine() calls.
     * @param soTimeOut timeout in milliseconds. If value = 0, taken to be infinite timeout
     * @throws java.io.IOException
     */
    public void acceptClient(int soTimeOut) throws IOException {
        this.printDebugMessages("Waiting for connection...");
        if (this.clients == null) {
            this.clients = new ArrayList<Socket>();
        }

        if (this.clientWriters == null) {
            this.clientWriters = new ArrayList<PrintWriter>();
        }


        if (this.clientReaders == null) {
            this.clientReaders = new ArrayList<BufferedReader>();
        }

        if (this.serverOutputStreams == null) {
            this.serverOutputStreams = new ArrayList<OutputStream>();
        }

        if (this.serverInputStreams == null) {
            this.serverInputStreams = new ArrayList<InputStream>();
        }

        Socket newClient = this.serverSocket.accept();

        newClient.setSoTimeout(soTimeOut);

        this.clients.add(newClient);

        // sets up client readers

        InputStream serverInputStream = newClient.getInputStream();
        this.serverInputStreams.add(serverInputStream);

        BufferedReader clientReader = new BufferedReader(
                new InputStreamReader(
                        serverInputStream
                ));

        this.clientReaders.add(clientReader);


        // sets up client writers

        OutputStream serverOutputStream = newClient.getOutputStream();
        this.serverOutputStreams.add(serverOutputStream);

        PrintWriter clientWriter = new PrintWriter(serverOutputStream, true);

        this.clientWriters.add(clientWriter);


        // Print clients connected
        int clientNumber = this.clients.size()- 1;

        this.printDebugMessages("Client " + clientNumber + " connected.");
    }

    /**
     * BLOCKS until it accepts a client or until the serverSOTimeout if set
     * @throws java.io.IOException
     */
    public void acceptClient() throws IOException {

        acceptClient(0);

    }


    //    Accessor methods to get the writers and readers. should not be needed
    @Deprecated
    public PrintWriter getWriterForClient(int index) {
        return this.clientWriters.get(index);
    }

    @Deprecated
    public BufferedReader getReaderForClient(int index) {
        return this.clientReaders.get(index);
    }

    public OutputStream getServerOutputStreamForClient(int index) {
        return this.serverOutputStreams.get(index);
    }

    public InputStream getServerInputStreamForClient(int index) {
        return this.serverInputStreams.get(index);
    }

    public void sendMessageToAllClients(String message) {
        for (int i = 0; i < this.clients.size(); i++) {
            if (this.clients.get(i) != null){
                this.sendMessageLineForClient(i, message);
            }

        }
    }

    public void sendMessageLineForClient(int index, String message) {

        PrintWriter writer = this.clientWriters.get(index);
        writer.println(message);
        writer.flush();

    }




    public String readMessageLineForClient(int index) throws IOException {

        this.printDebugMessages("Reading message from client " + index + "...");
        BufferedReader reader = this.clientReaders.get(index);
        try {

            String message = reader.readLine();
            this.printDebugMessages("Message received from client.");
            return message;
        } catch (SocketTimeoutException e) {
            System.err.println("Read timed out");
        }

        return "";

    }

    public void sendByteArrayForClient(int index, byte[] myByteArray) throws IOException {

//        if (len < 0)
//            throw new IllegalArgumentException("Negative length not allowed");
//        if (start < 0 || start >= myByteArray.length)
//            throw new IndexOutOfBoundsException("Out of bounds: " + start);
//        // Other checks if needed.

        // May be better to save the streams in the support class;
        // just like the socket variable.
        OutputStream out = this.getServerOutputStreamForClient(index);
        DataOutputStream dos = new DataOutputStream(out);

        dos.writeInt(myByteArray.length);
        if (myByteArray.length > 0) {
            dos.write(myByteArray, 0, myByteArray.length);
        }
    }

    public byte[] readByteArrayForClient(int index) throws IOException {
        InputStream in = this.getServerInputStreamForClient(index);
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    }

    /**
     * DOES NOT WORK PROPERLY IF YOU ADD CLIENTS AFTER RUNNING THIS ONCE
     * @return
     * @throws java.io.IOException
     */
    public String readMessageLineFromNextClient() throws IOException {

        int index = this.counter % this.clients.size();

        this.counter++;
        return this.readMessageLineForClient(index);

    }

//    /**
//     * Starts all the clientListeners with the default listener
//     */
//    public void startAllClientListeners() {
//        this.startAllClientListenersForListener(DefaultClientListenerRunnable.class);
//    }
//
//    /**
//     * Starts all listeners with a Runnable class parameter
//     * @param listener e.g. ClientListener.class
//     */
//    public void startAllClientListenersForListener(Class listener) {
//        for (int i = 0; i < this.clients.size(); i++) {
//
//            // Return if the iterable has been deleted
//            if (this.clients.get(i) == null) {
//                return;
//            }
//            this.startListenerForClient(listener, i);
//        }
//    }

//    /**
//     * Starts only the client at index for the default listener
//     * @param index
//     */
//    public void startListenerForClient(int index) {
//
//        startListenerForClient(DefaultClientListenerRunnable.class, index);
//    }
//
//    /**
//     * Starts listener for client at index with a Runnable class parameter
//     * @param listener a runnable, call Class.class
//     * @param index client number
//     */
//    public void startListenerForClient(Class listener, int index) {
////        Socket client = this.clients.get(index);
//
//        Runnable listenerInstance = null;
//
//        if (this.clientListenerThreads == null) {
//            this.clientListenerThreads = new ArrayList<Thread>();
//        }
//
//
//        // How do i pass Class as a parameter in java?
//        // http://stackoverflow.com/a/4873003
//
//        // Can I use Class.newInstance() with constructor arguments?
//        // http://stackoverflow.com/a/234617
//
//        try {
//
//            Class[] cArg = new Class[2]; //Our constructor has 2 arguments
//            cArg[0] = AwesomeSockets.AwesomeServerSocket.class;
//            cArg[1] = int.class; //Third argument is of *primitive* type int
//
//            listenerInstance = (Runnable)listener.getDeclaredConstructor(cArg).newInstance(this, index);
//            Thread clientListenerThread = new Thread(listenerInstance);
//            this.clientListenerThreads.add(clientListenerThread);
//            clientListenerThread.start();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//            System.err.println("Cannot start listener.");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//            System.err.println("Cannot start listener.");
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//            System.err.println("Cannot start listener.");
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//            System.err.println("Cannot start listener.");
//        }
//
//    }

    /**
     * Returns true if there are still threads running
     * @return
     */
    public boolean getListenerRunningState() {
        for (Thread thread : this.clientListenerThreads) {
            if (thread.isAlive()) {
                return true;
            }
        }

        // Sets a delay here so that we do not run the infinite loop so fast
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Closes the client sockets and the server
     * @throws java.io.IOException
     */
    public void closeServer() throws IOException {
        for (Socket client : this.clients) {
            if (client != null) {

                client.close();
            }
        }

        this.serverSocket.close();
        this.printDebugMessages("Server closed.");
    }

    /**
     * Sets the removed client to null
     * @param index
     */
    public void removeClient(int index) throws IOException {
        this.clients.set(index, null).close();
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
        String result = "";

        result += this.serverSocket.toString() + "\n";
        result += "Clients: ";

        for (Socket client : clients) {
            result += client.toString() + " ";
        }
        return result;
    }

    public static void main(String[] args) {

    }
}
