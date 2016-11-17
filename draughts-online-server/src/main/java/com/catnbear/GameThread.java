package com.catnbear;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class GameThread extends Thread {
    private static final String PLAYER_WHITE_ASSIGN_SERVER_MESSAGE = "w";
    private static final String PLAYER_BLACK_ASSIGN_SERVER_MESSAGE = "b";
    private static final String GAME_READY_SERVER_MESSAGE = "gameready";
    private static final String JOIN_NEW_PLAYER_CLIENT_MESSAGE = "join";
    private static final String PLAYER_START_GAME_WAIT_CLIENT_MESSAGE = "startready";
    private static final String PLAYER_NEXT_TURN_WAIT_CLIENT_MESSAGE = "turnready";
    private static final String PLAYER_DISCONNECTION_MESSAGE = "done";
    private static final long THREAD_SLEEP_TIME = 100;

    private Socket playerSocket;
    private int portNumber;
    private GameModel gameModel;
    private static int threadNumber;
    private String threadName;
    private int id;

    GameThread(Socket playerSocket, int portNumber) {
        this.playerSocket = playerSocket;
        this.portNumber = portNumber;
        gameModel = GameModel.getInstance();
        this.id = gameModel.getPlayerId();
        threadNumber++;
        threadName = "Thread " + threadNumber;
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
                System.out.println(threadName + " got message: " + inputLine);
                String outputLine = "";
                switch (inputLine) {
                    case JOIN_NEW_PLAYER_CLIENT_MESSAGE:
                        if (joinCounter % 2 == 0) {
                            outputLine = PLAYER_WHITE_ASSIGN_SERVER_MESSAGE;
                        } else {
                            outputLine = PLAYER_BLACK_ASSIGN_SERVER_MESSAGE;
                        }
                        break;
                    case PLAYER_START_GAME_WAIT_CLIENT_MESSAGE:
                        gameModel.addPlayer();
                        while(!gameModel.isGameReady()) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                        }
                        outputLine = GAME_READY_SERVER_MESSAGE;
                        break;
                    case PLAYER_NEXT_TURN_WAIT_CLIENT_MESSAGE:
                        while(!gameModel.isBoardAvailable(id)) {
                            Thread.sleep(THREAD_SLEEP_TIME);
                        }
                        outputLine = gameModel.getBoard();
                        break;
                    default:
                        System.out.println("Setting board: " + inputLine);
                        gameModel.setBoard(inputLine);
                        gameModel.setBoardAvailable(id);
                        break;
                }
                if (!outputLine.equals("")) {
                    System.out.println(threadName + " sending message: " + outputLine);
                    out.println(outputLine);
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