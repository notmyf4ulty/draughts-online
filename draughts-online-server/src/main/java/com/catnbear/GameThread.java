package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameThread extends Thread {
    private Socket socket;
    private int portNumber;
    private GameModel gameModel;

    public GameThread (Socket socket, int portNumber) {
        this.socket = socket;
        this.portNumber = portNumber;
        this.gameModel = GameModel.getInstance();
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String inputLine;
            do {
                System.out.println("waiting for new line");
                inputLine = in.readLine();
                System.out.println("Got: " + inputLine);
                switch (inputLine) {
                    case "wait":
                        while(!gameModel.isBoardAvailable()) {
                            Thread.sleep(100);
                        }
                        out.println(gameModel.getBoard());
                        break;
                    default:
                        gameModel.setBoard(inputLine);
                        gameModel.setBoardAvailable(true);
                        break;
                }
            } while (!inputLine.equals("done"));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}