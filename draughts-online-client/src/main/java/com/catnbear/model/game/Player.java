package com.catnbear.model.game;

public enum Player {
    WHITE("White"),
    BLACK("Black");

    private final String stringForm;

    Player(String stringForm) {
        this.stringForm = stringForm;
    }

    public String toString() {
        return stringForm;
    }
}
