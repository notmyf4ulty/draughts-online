package com.catnbear.gui;

import com.catnbear.model.game.Piece;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by przemek on 09.11.16.
 */
public class PieceView extends Circle implements Observer {
    Piece piece;
    GridPane board;

    public PieceView() {
        super();
    }

    public void bindPiece(Piece piece) {
        this.piece = piece;
        piece.addObserver(this);
    }

    public void assignBoard(GridPane board) {
        this.board = board;
    }

    @Override
    public void update(Observable observable, Object o) {

    }
}
