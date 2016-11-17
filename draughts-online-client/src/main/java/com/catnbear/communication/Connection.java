package com.catnbear.communication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import java.io.*;
import java.net.Socket;

/**
 * Connection - handling class. Used for the communication between the client and a server.
 */
public class Connection {
    /**
     * Singleton's instance.
     */
    private static Connection instance = null;

    /**
     * Server's host name.
     */
    private String hostName;

    /**
     * Server's port number.
     */
    private int portNumber;

    /**
     * Server's socket.
     */
    private Socket serverSocket;

    /**
     * Output to send messages to the server.
     */
    private PrintWriter outWriter;

    /**
     * Input to receive messages from the server.
     */
    private InputStream inputStream;

    /**
     * Buffer buffer for incoming data.
     */
    private String inputDataBuffer;

    /**
     * Flag indicating that data is ready to receive.
     */
    private BooleanProperty dataReadyFlag;

    /**
     * Default constructor. It's private because of the Singleton design pattern's purposes.
     */
    private Connection() {
        dataReadyFlag = new SimpleBooleanProperty(false);
    }

    /**
     * Getter of the Singleton's instance.
     * @return Class' instance.
     */
    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    /**
     * Sends data to the server.
     * @param data Data to be send.
     * @return True if data was sent properly. False otherwise.
     */
    public boolean sendData(String data) {
        System.out.println("Sending data: " + data);
        try {
            outWriter.println(data);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Launches DataReceiver's instance to cyclically check if there is data to receive.
     */
    public void waitForData() {
        DataReceiver dataReceiver = new DataReceiver();
        new Thread(dataReceiver).start();
    }

    /**
     * Setter of connection's parameters.
     * @param hostName New host name.
     * @param portNumber New port number.
     */
    public void setConnectionParameters(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Connects to the server.
     * @return True if connection process finished properly. False otherwise.
     */
    public boolean connect() {
        try {
            serverSocket = new Socket(hostName,portNumber);
            outWriter = new PrintWriter(serverSocket.getOutputStream(),true);
            inputStream = serverSocket.getInputStream();
            return true;
        } catch (IOException e) {
            System.out.println("Connection error.");
            return false;
        }
    }

    /**
     * Class for data receiving. Provides only one function - overridden version of the Task's call method.
     * It cyclically check whether there is a data to receive.
     */
    private class DataReceiver extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            System.out.print("Waiting for data...");
            inputDataBuffer = null;
            try {
                do {
                    if (inputStream.available() != 0) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        System.out.print(" Data ready: ");
                        inputDataBuffer = bufferedReader.readLine();
                        indicateDataReady();
                        System.out.println(inputDataBuffer);
                    }
                    Thread.sleep(500);
                } while (inputDataBuffer == null);
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            } catch (InterruptedException exception) {
                exception.printStackTrace();
                return null;
            }
            return null;
        }
    }

    /**
     * Adds listener for the dataReadyFlag.
     * @param booleanChangeListener New change's listener.
     */
    public void addDataReadyListener(ChangeListener<Boolean> booleanChangeListener) {
        dataReadyFlag.addListener(booleanChangeListener);
    }

    /**
     * Indicates potential listeners that there is a data to receive.
     */
    private void indicateDataReady() {
        if (inputDataBuffer != null) {
            System.out.println("Got shit: " + inputDataBuffer);
            dataReadyFlag.setValue(true);
            dataReadyFlag.setValue(false);
        }
    }

    /**
     * Getter of received data. After this operation, inputDataBuffer is flushed.
     * @return Received data.
     */
    public String getData() {
        String result = inputDataBuffer;
        inputDataBuffer = null;
        return result;
    }

    /**
     * Resets the connection.
     */
    public void resetConnection() {
        instance = null;
        hostName = null;
        portNumber = 0;
        serverSocket = null;
        outWriter = null;
        inputStream = null;
        inputDataBuffer = null;
        dataReadyFlag = null;
    }
}
