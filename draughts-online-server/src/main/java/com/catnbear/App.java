package com.catnbear;

import java.net.*;
import java.io.*;

public class App {
    private static int portNumber;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            portNumber = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(portNumber);

            // Infinite player's accepting loop.
            while (true) {
                try {
                    Socket clientSocket_1 = serverSocket.accept();
                    GameThread gameThread_1 = new GameThread(clientSocket_1, portNumber);
                    gameThread_1.start();
                    System.out.println("Client 1 connected.");

                    Socket clientSocket_2 = serverSocket.accept();
                    GameThread gameThread_2 = new GameThread(clientSocket_2, portNumber);
                    gameThread_2.start();
                    System.out.println("Client 2 connected.");
                } catch (IOException e) {
                    System.out.println("Connection problem. Resetting the game.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Inappropriate port number.");
        } catch (IOException e) {
            System.out.println("Cannot create socket.");
        }
    }
}
