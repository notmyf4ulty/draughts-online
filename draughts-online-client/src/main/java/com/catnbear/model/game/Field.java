package com.catnbear.model.game;

import java.util.Observable;

public class Field extends Observable {

    public enum FieldColor {
        COLOR_1,
        COLOR_2;
    }

    private final FieldColor fieldColor;
    private Piece piece;
    private final Position position;
    private BoardModel boardModel;

    public Field(FieldColor fieldColor, Position position) {
        this.fieldColor = fieldColor;
        this.piece = null;
        this.position = position;
    }

    public void canSelectPiece() {
        boardModel.selectPiece(position);
    }

    public void selectPiece() {

    }

    public void assignToBoard(BoardModel boardModel) {
        this.boardModel = boardModel;
    }

    public FieldColor getFieldColor() {
        return fieldColor;
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
