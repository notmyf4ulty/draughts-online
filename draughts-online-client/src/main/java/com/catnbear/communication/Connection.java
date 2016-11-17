package com.catnbear.communication;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;

public class Connection {
    private static Connection instance = null;
    private String hostName;
    private int portNumber;
    private Socket serverSocket;
    private PrintWriter outWriter;
    private InputStream inputStream;
    private String dataBuffer;
    private BooleanProperty dataReadyFlag;

    private Connection() {
        dataReadyFlag = new SimpleBooleanProperty(false);
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

    public void waitForData() {
        DataReceiver dataReceiver = new DataReceiver();
        new Thread(dataReceiver).start();
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

    private class DataReceiver extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            System.out.print("Waiting for data...");
            dataBuffer = null;
            try {
                do {
                    if (inputStream.available() != 0) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        System.out.print(" Data ready: ");
                        dataBuffer = bufferedReader.readLine();
                        indicateDataReady();
                        System.out.println(dataBuffer);
                    }
                    Thread.sleep(500);
                } while (dataBuffer == null);
            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            return null;
        }
    }

    public void addDataReadyListener(ChangeListener<Boolean> booleanChangeListener) {
        dataReadyFlag.addListener(booleanChangeListener);
    }

    private void indicateDataReady() {
        if (dataBuffer != null) {
            System.out.println("Got shit: " + dataBuffer);
            dataReadyFlag.setValue(true);
            dataReadyFlag.setValue(false);
        }
    }

    public String getData() {
        String result = dataBuffer;
        dataBuffer = null;
        return result;
    }

    public void resetConnection() {
        instance = null;
        hostName = null;
        portNumber = 0;
        serverSocket = null;
        outWriter = null;
        inputStream = null;
        dataBuffer = null;
        dataReadyFlag = null;
    }
}
