package com.catnbear.communication;

import java.io.*;
import java.net.Socket;

public class Connection {
    private static Connection instance = null;
    private String hostName;
    private int portNumber;
    private Socket serverSocket;

    private Connection() {}

    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public boolean sendData(String data) {
        try {
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(),true);
            out.println(data);
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    public String waitForData() {
        String response = "err";
        try {
            InputStream inputStream = serverSocket.getInputStream();
            do {
                if (inputStream.available() != 0) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    response = bufferedReader.readLine();
                }
                Thread.sleep(1000);
            } while (response.equals("err"));
        } catch (IOException exception) {
            return response;
        } catch (InterruptedException exception) {
            return response;
        }
        System.out.println(response);
        return response;
    }

    public void setConnectionParameters(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    public void connect() {
        try {
            serverSocket = new Socket(hostName,portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
