package com.catnbear.model.game;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ThreadLocalRandom;

public class GameModel {
    private static GameModel instance = null;
    private Player activePlayer;
    private StringProperty activePlayerLabelText;
    private boolean moveAvailable;
    private Board board;
    private IntegerProperty roundLabelValue;

    private GameModel() {
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

    Player getActivePlayer() {
        return activePlayer;
    }

    public void prepareNewRound() {
        if (!moveAvailable) {
            if (roundLabelValue == null) {
                activePlayer = drawPlayer();
                activePlayerLabelText = new SimpleStringProperty(activePlayer.toString());
                roundLabelValue = new SimpleIntegerProperty(0);
            } else {
                nextPlayer();
                roundLabelValue.setValue(roundLabelValue.getValue() + 1);
            }
            moveAvailable = true;
            backupBoardModel();
        }
    }

    private void nextPlayer() {
        if (activePlayer.equals(Player.WHITE)) {
            activePlayer = Player.BLACK;
        } else {
            activePlayer = Player.WHITE;
        }
        activePlayerLabelText.setValue(activePlayer.toString());
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
