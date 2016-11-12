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

    public Field getCopy() {
        Field field = new Field(this.position.getCopy());
        if (containsPiece()) {
            Piece piece = this.piece.getCopy();
            piece.assignField(field);
        }
        return field;
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
