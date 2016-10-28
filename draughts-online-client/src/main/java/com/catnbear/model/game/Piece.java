package com.catnbear.model.game;

public class Piece {

    public enum PieceOwner {
        PLAYER_1,
        PLAYER_2;
    }

    public enum PieceType {
        MEN,
        KING;
    }

    private PieceType type;
    private PieceOwner owner;
    private Position position;

    public Piece(PieceType type, PieceOwner owner, Position position) {
        this.type = type;
        this.owner = owner;
        this.position = position;
    }
}
