package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameThread extends Thread {
    private Socket firstSocket;
    private Socket secondSocket;
    private int portNumber;

    public GameThread (Socket firstSocket, Socket secondSocket, int portNumber) {
        this.firstSocket = firstSocket;
        this.secondSocket = secondSocket;
        this.portNumber = portNumber;
    }

    @Override
    public void run() {
        try (
                PrintWriter firstSocketOut =
                        new PrintWriter(firstSocket.getOutputStream(), true);
                BufferedReader firstSocketIn = new BufferedReader(
                        new InputStreamReader(firstSocket.getInputStream()));
                PrintWriter secondSocketOut =
                        new PrintWriter(secondSocket.getOutputStream(), true);
                BufferedReader secondSocketIn = new BufferedReader(
                        new InputStreamReader(secondSocket.getInputStream()))
        ) {
            String inputLine;
            while(true) {
                firstSocketIn.readLine();
                firstSocketOut.println("ok");
                secondSocketIn.readLine();
                secondSocketOut.println("ok");
                secondSocketOut.println(firstSocketIn.readLine());
                secondSocketOut.println("")
            }
            while ((inputLine = firstSocketIn.readLine()) != null) {
                System.out.println(inputLine);
                firstSocketOut.println(inputLine);
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