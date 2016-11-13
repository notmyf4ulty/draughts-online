package com.catnbear;

public class GameModel {
    private static GameModel instance;
    private int joinCounter;

    private GameModel() {}

    static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    int getJoinCounter() {
        return joinCounter++;
    }
}
