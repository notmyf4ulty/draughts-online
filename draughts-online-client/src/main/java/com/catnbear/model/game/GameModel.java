package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import javafx.application.Platform;
import javafx.beans.property.*;

public class GameModel {
    private static GameModel instance = null;
    private Player player;
    private StringProperty activePlayerLabelText;
    private boolean moveAvailable;
    private Board board;
    private Connection connection;
    private GameStatus gameStatus;

    private GameModel() {
        gameStatus = new GameStatus();
        activePlayerLabelText = new SimpleStringProperty("");
    }

    public static GameModel getInstance() {
        if(instance == null) {
            System.out.println("Starting new game.");
            instance = new GameModel();
        }
        return instance;
    }

    Player getPlayer() {
        return player;
    }

    public void startNewGame() {
        gameStatus.setStatusState(GameStatus.StatusState.CONNECTING_TO_SERVER);
        if (establishConnection()) {
            joinGame();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    private boolean establishConnection() {
        connection = Connection.getInstance();
        initializeConnectionHandlers();
        connection.setConnectionParameters("localhost",10001);
        return connection.connect();
    }

    private void joinGame() {
        gameStatus.setStatusState(GameStatus.StatusState.JOINING_GAME);
        connection.sendData("join");
        connection.waitForData();
    }

    private void handleDataReady() {
        String receivedData = connection.getData();
        if (receivedData != null) {
            System.out.println("Handling received data: " + receivedData);
            switch (gameStatus.getStatusState()) {
                case NOT_STARTED:
                    break;
                case CONNECTING_TO_SERVER:
                    break;
                case JOINING_GAME:
                    gameJoined(receivedData);
                    break;
                case WAITING_FOR_SECOND_PLAYER:
                    secondPlayerReady(receivedData);
                    break;
                case WAITING_FOR_TURN:
                    newRound(receivedData);
                    break;
                case TURN:
                    break;
                case LOST:
                    break;
                case WON:
                    break;
                case CONNECTION_ERROR:
                    break;
            }
        }
    }

    private void gameJoined(String playerString) {
        switch (playerString) {
            case "w":
                player = Player.WHITE;
                break;
            case "b":
                player = Player.BLACK;
                break;
            default:
                player = Player.INCORRECT_PLAYER;
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                break;
        }
        if (!player.equals(Player.INCORRECT_PLAYER)) {
            Platform.runLater(() -> activePlayerLabelText.setValue(player.toString()));
            waitForNextPlayer();
        }
    }

    private void waitForNextPlayer() {
        gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_SECOND_PLAYER);
        connection.sendData("startready");
        connection.waitForData();
    }

    private void secondPlayerReady(String gameReadyMessage) {
        if (gameReadyMessage.equals("gameready")) {
            startGame();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    private void startGame() {
        moveAvailable = true;
        switch (player) {
            case WHITE:
                gameStatus.setStatusState(GameStatus.StatusState.TURN);
                break;
            case BLACK:
                gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_TURN);
                waitForFirstRound();
                break;
            case INCORRECT_PLAYER:
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                break;
        }
    }

    private void waitForFirstRound() {
        connection.sendData("turnready");
        connection.waitForData();
    }

    public void prepareNewRound() {
        if (!moveAvailable) {
            backupBoardModel();
            endTurn();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.TURN);
        }
    }

    private void endTurn() {
        int enemyPiecesNumber = board.countPieces();
        if (enemyPiecesNumber != 0) {
            gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_TURN);
            connection.sendData(board.prepareToSend());
            connection.sendData("turnready");
            connection.waitForData();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.WON);
            connection.sendData("won");
        }
    }

    private void backupBoardModel() {
        if (board != null) {
            board.backupBoard();
        }
    }

    public void retreiveBackup() {
        if (board != null) {
            board.retrieveBackup();
        }
        moveAvailable = true;
    }

    private void initializeConnectionHandlers() {
        connection.addConnectionErrorListener((observableValue, aBoolean, t1) ->
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR));
        connection.addDataReadyListener((observablshiteValue, aBoolean, t1) -> handleDataReady());
    }

    private void newRound(String boardString) {
        Platform.runLater(() -> {
            switch (boardString) {
                case "lost":
                    gameStatus.setStatusState(GameStatus.StatusState.LOST);
                    break;
                case "won":
                    gameStatus.setStatusState(GameStatus.StatusState.WON);
                    break;
                default:
                    if (board.createBoardFromString(boardString)) {
                        gameStatus.setStatusState(GameStatus.StatusState.TURN);
                        moveAvailable = true;
                    } else {
                        gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                    }
                    break;
            }
        });
    }

    public void surrender() {
        gameStatus.setStatusState(GameStatus.StatusState.LOST);
        connection.sendData("lost");
    }

    public void exit() {
        gameStatus.setStatusState(GameStatus.StatusState.EXIT);
    }

    public void assignBoardModel(Board board) {
        this.board = board;
    }

    public boolean isMoveAvailable() {
        return moveAvailable;
    }

    public void setMoveAvailable(boolean moveAvailable) {
        this.moveAvailable = moveAvailable;
    }

    public StringProperty activePlayerLabelTextProperty() {
        return activePlayerLabelText;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void resetGameModel() {
        instance = null;
        player = null;
        activePlayerLabelText = null;
        moveAvailable = false;
        board = null;
        connection.resetConnection();
        gameStatus = null;
    }
}
