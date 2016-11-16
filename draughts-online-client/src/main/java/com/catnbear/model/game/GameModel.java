package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import com.catnbear.utilities.GuiModifier;
import javafx.beans.property.*;

public class GameModel {
    private static GameModel instance = null;
    private Player player;
    private StringProperty activePlayerLabelText;
    private boolean moveAvailable;
    private Board board;
    private boolean gameStarted;
    private Connection connection;
    private GameStatus gameStatus;

    private GameModel() {
        gameStatus = new GameStatus();
        activePlayerLabelText = new SimpleStringProperty("");
    }

    public static GameModel getInstance() {
        if(instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    Player getPlayer() {
        return player;
    }

    private boolean establishConnection() {
        connection = Connection.getInstance();
        connection.setConnectionParameters("localhost",10001);
        return connection.connect();
    }

    public void startNewGame() {
        gameStatus.setStatusState(GameStatus.StatusState.CONNECTING_TO_SERVER);
        if (establishConnection()) {
            gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_SECOND_PLAYER);
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    public void prepareNewRound() {
        if (!moveAvailable) {
            if (!gameStarted) {
                initializeGame();
            } else {
                connection.sendData(board.prepareToSend());
                waitForNextRound();
            }
            moveAvailable = true;
            backupBoardModel();
        }
    }

    private Player joinGame() {
        connection.sendData("join");
        String response = connection.waitForData();
        Player player;
        switch (response) {
            case "w":
                player = Player.WHITE;
                break;
            case "b":
                player = Player.BLACK;
                break;
            default:
                player = Player.INCORRECT_PLAYER;
                break;
        }
        return player;
    }

    private void initializeGame() {
        player = joinGame();
        activePlayerLabelText.setValue(player.toString());
        gameStarted = true;
        if (player.equals(Player.BLACK)) {
            waitForNextRound();
        }

    }

    private void waitForNextRound() {
        connection.sendData("wait");
        String response = connection.waitForData();
        System.out.println("Got response: " + response);
        board.createBoardFromString(response);
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

    public void surrender() {

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
}
