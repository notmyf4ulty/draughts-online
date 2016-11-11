package com.catnbear.gui;

import com.catnbear.model.game.Piece;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class PieceView extends Circle {
    Piece piece;
    private static final int PIECE_RADIUS = 23;

    public PieceView(Piece piece) {
        super();
        this.piece = piece;
        assignColor(piece.getOwner());
        setRadius(PIECE_RADIUS);
    }

    private void assignColor(Piece.PieceOwner pieceOwner) {
        String color;
        switch (pieceOwner) {
            case PLAYER_1:
                color = "white";
                break;
            case PLAYER_2:
                color = "black";
                break;
            default:
                color = "";
                break;
        }
        setFill(Paint.valueOf(color));
    }
}
