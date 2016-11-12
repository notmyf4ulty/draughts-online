package com.catnbear.model.game;

public enum Player {
    PLAYER_1("Player 1"),
    PLAYER_2("Player 2");

    private final String stringForm;

    Player(String stringForm) {
        this.stringForm = stringForm;
    }

    public String toString() {
        return stringForm;
    }
}
