package com.catnbear.model.game;

/**
 * Possible player choices.
 */
public enum Player {
    WHITE("White"),
    BLACK("Black"),
    INCORRECT_PLAYER("Black");

    private final String stringForm;

    Player(String stringForm) {
        this.stringForm = stringForm;
    }

    public String toString() {
        return stringForm;
    }
}
