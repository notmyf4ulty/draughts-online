package com.catnbear;

public class GameModel {
    private static GameModel instance;
    volatile private int joinCounter;
    volatile private String board;
    private volatile boolean boardAvailable;
    volatile boolean gameFinished;

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

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public boolean isBoardAvailable() {
//        if (boardAvailable) {
//            boardAvailable = false;
//        }
//        return true;
        return boardAvailable;
    }

    public void setBoardAvailable(boolean boardAvailable) {
        this.boardAvailable = boardAvailable;
    }
}
