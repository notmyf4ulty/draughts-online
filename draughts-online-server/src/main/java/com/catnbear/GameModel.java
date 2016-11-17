package com.catnbear;

class GameModel {
    private static GameModel instance;
    volatile private int joinCounter;
    volatile private String board;
    private volatile boolean boardAvailable;
    volatile boolean gameFinished;
    private volatile int playerCounter;
    private volatile int playerId;
    private volatile int lastUpdatePlayerId;

    private GameModel() {
    }

    static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    int getJoinCounter() {
        return joinCounter++;
    }

    String getBoard() {
        boardAvailable = false;
        return board;
    }

    void setBoard(String board) {
        this.board = board;
    }

    boolean isBoardAvailable(int playerId) {
        return (lastUpdatePlayerId != playerId) && boardAvailable;
    }

    void addPlayer() {
        if (++playerCounter > 2) {
            playerCounter = 1;
        }
    }

    boolean isGameReady() {
        return playerCounter == 2;
    }

    int getPlayerId() {
        return playerId++;
    }

    void setBoardAvailable(int updatePlayerId) {
        lastUpdatePlayerId = updatePlayerId;
        boardAvailable = true;
    }
}
