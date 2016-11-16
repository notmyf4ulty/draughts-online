package com.catnbear;

class GameModel {
    private static GameModel instance;
    volatile private int joinCounter;
    volatile private String board;
    private volatile boolean boardAvailable;
    volatile boolean gameFinished;
    volatile int playerCounter;

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
        return board;
    }

    void setBoard(String board) {
        this.board = board;
    }

    boolean isBoardAvailable() {
//        if (boardAvailable) {
//            boardAvailable = false;
//        }
//        return true;
        return boardAvailable;
    }

    void addPlayer() {
        if (++playerCounter > 2) {
            playerCounter = 1;
        }
    }

    boolean isGameReady() {
        return playerCounter == 2;
    }

    void setBoardAvailable(boolean boardAvailable) {
        this.boardAvailable = boardAvailable;
    }
}
