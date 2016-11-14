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
            do {
                inputLine = in.readLine();
                System.out.println("Got: " + inputLine);
                switch (inputLine) {
                    case "join":
                        if (joinCounter % 2 == 0) {
                            out.println("w");
                        } else {
                            out.println("b");
                        }
                        break;
                    case "wait":
                        while(!gameModel.isBoardAvailable()) {
                            Thread.sleep(100);
                        }
                        System.out.println("Sending board: " + gameModel.getBoard());
                        out.println(gameModel.getBoard());
                        gameModel.setBoardAvailable(false);
                        break;
                    default:
                        gameModel.setBoard(inputLine);
                        gameModel.setBoardAvailable(true);

                        break;
                }
                Thread.sleep(100);
            } while (!inputLine.equals("done"));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Finishing configuration.");
    }
}