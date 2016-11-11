package com.catnbear.model.game;

import java.util.Observable;

public class Piece extends Observable {

    public enum PieceOwner {
        NONE,
        PLAYER_1,
        PLAYER_2
    }

    public enum PieceType {
        NONE,
        MEN,
        KING
    }

    private PieceType type;
    private PieceOwner owner;
    private Position position;

    public Piece(PieceType type, PieceOwner owner, Position position) {
        this.type = type;
        this.owner = owner;
        this.position = position;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
    }

    public PieceOwner getOwner() {
        return owner;
    }

    public void setOwner(PieceOwner owner) {
        this.owner = owner;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
