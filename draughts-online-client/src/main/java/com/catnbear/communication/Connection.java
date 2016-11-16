package com.catnbear.communication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

import java.io.*;
import java.net.Socket;

public class Connection {
    private static final long WAIT_FOR_DATA_TIMEOUT = 5000;

    private static Connection instance = null;
    private String hostName;
    private int portNumber;
    private Socket serverSocket;
    private PrintWriter outWriter;
    private InputStream inputStream;
    private BooleanProperty connectionErrorFlag;


    private Connection() {
        connectionErrorFlag = new SimpleBooleanProperty(false);
    }

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public boolean sendData(String data) {
        System.out.println("Sending data: " + data);
        outWriter.println(data);
        return true;
    }

    public String waitForData() {
        System.out.print("Waiting for data...");
        String response = "err";
        try {
            long waitStartTime = System.currentTimeMillis();
            do {
                if (inputStream.available() != 0) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    System.out.print(" Data ready: ");
                    response = bufferedReader.readLine();
                    System.out.println(response);
                }
                Thread.sleep(1000);
            } while (response.equals("err"));// && isTimeout(waitStartTime));
        } catch (IOException exception) {
            exception.printStackTrace();
            return response;
        } catch (InterruptedException exception) {
            exception.printStackTrace();
            return response;
        }
        System.out.println(response);
        return response;
    }

    public void setConnectionParameters(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

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

    private boolean isTimeout(long startTime) {
        return System.currentTimeMillis() - startTime < WAIT_FOR_DATA_TIMEOUT;
    }

    public void addConnectionListener(ChangeListener<Boolean> booleanChangeListener) {
        connectionErrorFlag.addListener(booleanChangeListener);
    }

    public void removeConnectionListener(ChangeListener<Boolean> booleanChangeListener) {
        connectionErrorFlag.removeListener(booleanChangeListener);
    }
}
