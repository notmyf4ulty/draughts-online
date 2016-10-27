package com.catnbear.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by przemek on 27.10.16.
 */
public class CommunicationModel {
    private static CommunicationModel instance;
    private Socket echoSocket;
    private StringProperty stringProperty;


    private CommunicationModel() {
        initialize();
    }

    private void initialize() {

        String hostName = "52.57.131.200";
        int portNumber = 10001;

        stringProperty = new SimpleStringProperty("");

        try {
            echoSocket = new Socket(hostName, portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AutoAsker autoAsker = new AutoAsker();
        new Thread(autoAsker).start();
    }

    public static CommunicationModel getInstance() {
        if (instance == null) {
            instance = new CommunicationModel();
        }
        return instance;
    }



    public Socket getEchoSocket() {
        return echoSocket;
    }

    public String getStringProperty() {
        return stringProperty.get();
    }

    public StringProperty stringPropertyProperty() {
        return stringProperty;
    }

    private class AutoAsker extends Task<Void> {
        @Override
        public Void call() {
            try {
                InputStream inputStream = echoSocket.getInputStream();
                do {
                    stringProperty.setValue("");
                    System.out.println(inputStream.available());
                    if (inputStream.available() != 0) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        stringProperty.setValue(bufferedReader.readLine());
                    }
                    Thread.sleep(1000);
                } while (!stringProperty.getValue().equals("exit"));
//                inputStream.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
