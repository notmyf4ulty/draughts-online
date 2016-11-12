package com.catnbear.model.game;

import java.util.Observable;

public class Field extends Observable {

    private Piece piece;
    private final Position position;

    Field(Position position) {
        this.piece = null;
        this.position = position;
    }

    boolean isPieceSelcted() {
        return piece.isSelected();
    }

    void selectPiece() {
        piece.select();
    }

    void unselectPiece() {
        piece.unselect();
    }

    void resetPiece() {
        piece = null;
    }

    public Position getPosition() {
        return position;
    }

    public boolean containsPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    void setPiece(Piece piece) {
        this.piece = piece;
    }
}
