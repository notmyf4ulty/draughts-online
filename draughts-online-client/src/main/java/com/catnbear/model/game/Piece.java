package com.catnbear.model.game;

import java.util.Observable;

public class Piece extends Observable {

    public enum PieceType {
        NONE,
        MEN,
        KING
    }

    private PieceType type;
    private Player player;
    private boolean selected;
    private Field field;

    public Piece(PieceType type, Player player) {
        this.type = type;
        this.player = player;
    }

    public void assignField(Field field) {
        if (this.field != null) {
            this.field.resetPiece();
        }
        this.field = field;
        this.field.setPiece(this);
    }

    void select() {
        selected = true;
    }

    void unselect() {
        selected = false;
    }

    public boolean isSelected() {
        return selected;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
