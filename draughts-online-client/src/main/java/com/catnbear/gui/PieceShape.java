package com.catnbear.gui;

import com.catnbear.model.game.Piece;
import com.catnbear.model.game.Player;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Piece shape reflecting a logical piece object.
 */
class PieceShape extends Circle {

    /**
     * Piece's model.
     */
    private Piece piece;

    /**
     * Shape's radius.
     */
    private static final int PIECE_RADIUS = 23;

    /**
     * Constructor creating an appropriate shape regarding to a given Piece's object.
     * @param piece Piece's instance.
     */
    PieceShape(Piece piece) {
        super();
        this.piece = piece;
        assignColor(piece.getPlayer());
        setRadius(PIECE_RADIUS);
    }

    /**
     * Assigns a color to the shape regarding to the piece's owning player.
     * @param player Player which owns a piece.
     */
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
        if (piece.isSelectedFlag()) {
            boundsColor = "blue";
        } else {
            boundsColor = insideColor;
        }
        setFill(Paint.valueOf(insideColor));
        setStroke(Paint.valueOf(boundsColor));
    }
}
