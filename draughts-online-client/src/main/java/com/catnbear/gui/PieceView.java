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
        String insideColor;
        String boundsColor;
        switch (pieceOwner) {
            case PLAYER_1:
                insideColor = "white";
                break;
            case PLAYER_2:
                insideColor = "black";
                break;
            default:
                insideColor = "";
                break;
        }
        if (piece.isSelected()) {
            boundsColor = "blue";
        } else {
            boundsColor = insideColor;
        }
        setFill(Paint.valueOf(insideColor));
        setStroke(Paint.valueOf(boundsColor));
    }
}
