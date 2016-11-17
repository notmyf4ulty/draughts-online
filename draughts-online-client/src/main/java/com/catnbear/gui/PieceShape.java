package com.catnbear.gui;

import com.catnbear.model.game.Piece;
import com.catnbear.model.game.Player;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

class PieceShape extends Circle {
    private Piece piece;
    private static final int PIECE_RADIUS = 23;

    PieceShape(Piece piece) {
        super();
        this.piece = piece;
        assignColor(piece.getPlayer());
        setRadius(PIECE_RADIUS);
    }

    private void assignColor(Player player) {
        String insideColor;
        String boundsColor;
        switch (player) {
            case WHITE:
                insideColor = "white";
                break;
            case BLACK:
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
