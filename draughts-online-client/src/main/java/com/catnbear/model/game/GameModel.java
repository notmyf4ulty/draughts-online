package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ThreadLocalRandom;

public class GameModel {
    private static GameModel instance = null;
    private Player player;
    private StringProperty activePlayerLabelText;
    private boolean moveAvailable;
    private Board board;
    private IntegerProperty roundLabelValue;
    private Connection connection;

    private GameModel() {
        connection = Connection.getInstance();
        connection.setConnectionParameters("localhost",10001);
        connection.connect();
        prepareNewRound();
    }

    public static GameModel getInstance() {
        if(instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    private Player drawPlayer() {
        return (ThreadLocalRandom.current().nextInt(0,2) == 0) ? Player.WHITE : Player.BLACK;
    }

    Player getPlayer() {
        return player;
    }

    public void prepareNewRound() {
        if (!moveAvailable) {
            if (roundLabelValue == null) {
                initializeGame();
            } else {
                waitForNextRound();
//                nextRound();
                roundLabelValue.setValue(roundLabelValue.getValue() + 1);
            }
            moveAvailable = true;
            backupBoardModel();
        }
//        if (board != null) {
//            connection.sendData(board.prepareToSend());
//            board.createBoardFromString(connection.waitForData());
//        }
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
        activePlayerLabelText = new SimpleStringProperty(player.toString());
        roundLabelValue = new SimpleIntegerProperty(0);
    }

    private boolean waitForNextRound() {
        connection.sendData("ready");
        String response = connection.waitForData();
        return response.equals("ok");
    }

    private void nextRound() {
        if (player.equals(Player.WHITE)) {
            player = Player.BLACK;
        } else {
            player = Player.WHITE;
        }
        activePlayerLabelText.setValue(player.toString());
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
