package com.catnbear.model.game;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.concurrent.ThreadLocalRandom;

public class GameModel {
    private static GameModel instance = null;
    private Player activePlayer;
    private StringProperty activePlayerProperty;

    private GameModel() {
        activePlayer = drawPlayer();
        activePlayerProperty = new SimpleStringProperty(activePlayer.toString());
    }

    public static GameModel getInstance() {
        if(instance == null) {
            instance = new GameModel();
        }
        return instance;
    }


    private Player drawPlayer() {
        return (ThreadLocalRandom.current().nextInt(0,2) == 0) ? Player.PLAYER_1 : Player.PLAYER_2;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void nextPlayer() {
        if (activePlayer.equals(Player.PLAYER_1)) {
            activePlayer = Player.PLAYER_2;
        } else {
            activePlayer = Player.PLAYER_1;
        }
        activePlayerProperty.setValue(activePlayer.toString());
    }

    public String getActivePlayerProperty() {
        return activePlayerProperty.get();
    }

    public StringProperty activePlayerPropertyProperty() {
        return activePlayerProperty;
    }
}
