package com.catnbear.communication;

import java.io.*;
import java.net.Socket;

public class Connection {
    private static Connection instance = null;
    private String hostName;
    private int portNumber;
    private Socket serverSocket;
    private PrintWriter outWriter;
    private InputStream inputStream;

    private Connection() {}

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
        String response = "err";
        try {
            do {
                if (inputStream.available() != 0) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    response = bufferedReader.readLine();
                }
                Thread.sleep(1000);
            } while (response.equals("err"));
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
}
