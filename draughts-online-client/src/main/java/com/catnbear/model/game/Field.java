package com.catnbear.model.game;

import java.util.Observable;

public class Field extends Observable {

    private Piece piece;
    private final Position position;
    private BoardModel boardModel;

    public Field(Position position) {
        this.piece = null;
        this.position = position;
    }

    public boolean isPieceSelcted() {
        return piece.isSelected();
    }

    public void selectPiece() {
        piece.select();
    }

    public void unselectPiece() {
        piece.unselect();
    }

    public void resetPiece() {
        piece = null;
    }

    public void assignToBoard(BoardModel boardModel) {
        this.boardModel = boardModel;
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

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
