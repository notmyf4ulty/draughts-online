package com.catnbear.model.game;

/**
 * Created by przemek on 09.11.16.
 */
public class GameModel {
    private static GameModel instance = null;
    private BoardModel boardModel;

    private GameModel() {
        boardModel = new BoardModel();
    }

    public static GameModel getInstance() {
        if(instance == null) {
            instance = new GameModel();
        }
        return instance;
    }



    public BoardModel getBoardModel() {
        return boardModel;
    }
}
