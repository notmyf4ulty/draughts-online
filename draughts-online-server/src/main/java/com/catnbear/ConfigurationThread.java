package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConfigurationThread extends Thread {
    private Socket mySocket;
    private int portNumber;
    private GameModel gameModel;

    public ConfigurationThread(Socket mySocket, int portNumber) {
        this.mySocket = mySocket;
        this.portNumber = portNumber;
        gameModel = GameModel.getInstance();
    }

    @Override
    public void run() {
        try (
                PrintWriter out =
                        new PrintWriter(mySocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(mySocket.getInputStream()))
        ) {
            int joinCounter = gameModel.getJoinCounter();
            String inputLine;
            inputLine = in.readLine();
            if (inputLine.equals("join")) {
                if (joinCounter % 2 == 0) {
                    out.println("w");
                } else {
                    out.println("b");
                }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
        System.out.println("Finishing configuration.");
    }
}