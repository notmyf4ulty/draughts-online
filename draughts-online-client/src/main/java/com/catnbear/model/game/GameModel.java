package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import javafx.beans.property.*;

import java.util.concurrent.ThreadLocalRandom;

public class GameModel {
    private static GameModel instance = null;
    private Player player;
    private StringProperty activePlayerLabelText;
    private boolean moveAvailable;
    private Board board;
    private IntegerProperty roundLabelValue;
    private Connection connection;
    private BooleanProperty connectionLost;

    private GameModel() {
        connectionLost = new SimpleBooleanProperty(false);
        connection = Connection.getInstance();
        connection.setConnectionParameters("localhost",10001);
        connectionLost.setValue(connection.connect());
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

    public void prepareNewRound() {
        if (!moveAvailable) {
            if (roundLabelValue == null) {
                initializeGame();
            } else {
                connection.sendData(board.prepareToSend());
                waitForNextRound();
                roundLabelValue.setValue(roundLabelValue.getValue() + 1);
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
        if (player.equals(Player.BLACK)) {
            waitForNextRound();
        }
        activePlayerLabelText = new SimpleStringProperty(player.toString());
        roundLabelValue = new SimpleIntegerProperty(0);
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

    public IntegerProperty roundLabelValueProperty() {
        return roundLabelValue;
    }
}
