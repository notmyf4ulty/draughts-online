package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import com.catnbear.utilities.GuiModifier;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
        initializeConnecionErrorHandler();
        connection.setConnectionParameters("localhost",10001);
        return connection.connect();
    }

    public void startNewGame() {
        gameStatus.setStatusState(GameStatus.StatusState.CONNECTING_TO_SERVER);
        if (establishConnection()) {
            prepareNewRound();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    public void prepareNewRound() {
        if (!moveAvailable) {
            if (!gameStarted) {
                initializeGame();
            } else {
                waitForNextRound();
            }
            moveAvailable = true;
            backupBoardModel();
        }
    }

    private void initializeGame() {
        player = joinGame();
        if (!player.equals(Player.INCORRECT_PLAYER)) {
            activePlayerLabelText.setValue(player.toString());
            gameStarted = true;
            waitForNextPlayer();
            if (player.equals(Player.BLACK)) {
                waitForNextRound();
            }
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
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

    private void waitForNextPlayer() {
        gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_SECOND_PLAYER);
        connection.sendData("startready");
        String response = connection.waitForData();
        if (response.equals("gameready")) {
            gameStatus.setStatusState(GameStatus.StatusState.TURN);
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    private void waitForNextRound() {
        gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_TURN);
        connection.sendData(board.prepareToSend());
        connection.sendData("turnready");
        String response = connection.waitForData();
        System.out.println("Got response: " + response);
        board.createBoardFromString(response);
        gameStatus.setStatusState(GameStatus.StatusState.TURN);
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

    private void initializeConnecionErrorHandler() {
        connection.addConnectionListener((observableValue, aBoolean, t1) ->
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR));
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
