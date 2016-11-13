package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameThread extends Thread {
    private Socket mySocket;
    private Socket herSocket;
    private int portNumber;

    public GameThread (Socket mySocket, Socket herSocket, int portNumber) {
        this.mySocket = mySocket;
        this.herSocket = herSocket;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try (
                PrintWriter gameOut =
                        new PrintWriter(herSocket.getOutputStream(), true);
                BufferedReader gameIn = new BufferedReader(
                        new InputStreamReader(mySocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = gameIn.readLine()) != null) {
                    System.out.println(inputLine);
                    gameOut.println(inputLine);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}