package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class GameThread extends Thread {
    private static final String PLAYER_WHITE_ASSIGN_MESSAGE = "w";
    private static final String PLAYER_BLACK_ASSIGN_MESSAGE = "b";
    private static final String JOIN_NEW_PLAYER_MESSAGE = "join";
    private static final String PLAYER_WAITS_MESSAGE = "wait";
    private static final String PLAYER_DISCONNECTION_MESSAGE = "done";
    private static final long THREAD_SLEEP_TIME = 100;

    private Socket playerSocket;
    private int portNumber;
    private GameModel gameModel;

    GameThread(Socket playerSocket, int portNumber) {
        this.playerSocket = playerSocket;
        this.portNumber = portNumber;
        gameModel = GameModel.getInstance();
    }

    @Override
    public void run() {
        try (
                PrintWriter out =
                        new PrintWriter(playerSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(playerSocket.getInputStream()))
        ) {
            int joinCounter = gameModel.getJoinCounter();
            String inputLine;
            do {
                inputLine = in.readLine();
                switch (inputLine) {
                    case JOIN_NEW_PLAYER_MESSAGE:
                        if (joinCounter % 2 == 0) {
                            out.println(PLAYER_WHITE_ASSIGN_MESSAGE);
                        } else {
                            out.println(PLAYER_BLACK_ASSIGN_MESSAGE);
                        }
                        break;
                    case PLAYER_WAITS_MESSAGE:
                        while(!gameModel.isBoardAvailable()) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                        }
                        out.println(gameModel.getBoard());
                        gameModel.setBoardAvailable(false);
                        break;
                    default:
                        gameModel.setBoard(inputLine);
                        gameModel.setBoardAvailable(true);
                        break;
                }
                Thread.sleep(THREAD_SLEEP_TIME);
            } while (!inputLine.equals(PLAYER_DISCONNECTION_MESSAGE));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}