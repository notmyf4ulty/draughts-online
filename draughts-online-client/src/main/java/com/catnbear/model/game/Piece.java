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
    private Position position;
    private boolean selected;

    public Piece(PieceType type, Player player, Position position) {
        this.type = type;
        this.player = player;
        this.position = position;
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

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
