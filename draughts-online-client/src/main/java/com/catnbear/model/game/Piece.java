package com.catnbear.model.game;

import java.util.Observable;

/**
 * Piece's class. Extends an Observable class which allows it to be observed.
 */
public class Piece extends Observable {

    /**
     * Piece's owner.
     */
    private Player player;

    /**
     * Indicates if the piece is selectedFlag.
     */
    private boolean selectedFlag;

    /**
     * Assigned field.
     */
    private Field field;

    /**
     * Class' constructor.
     * @param player Piece's owning player.
     */
    Piece(Player player) {
        this.player = player;
    }

    /**
     * Assigns the piece to the given field.
     * @param field Field of the piece to be assigned to.
     */
    void assignField(Field field) {
        if (this.field != null) {
            this.field.clearPiece();
        }
        this.field = field;
        this.field.setPiece(this);
    }

    /**
     * Getter of the object's copy.
     * @return A copy of the Piece object.
     */
    Piece getCopy() {
        Piece piece = new Piece(player);
        piece.selectedFlag = selectedFlag;
        return piece;
    }

    /**
     * Selects a piece.
     */
    void select() {
        selectedFlag = true;
    }

    /**
     * Unselects a piece.
     */
    void unselect() {
        selectedFlag = false;
    }

    /**
     * Flag indicating whether piece is selected.
     * @return True if it is selected. False otherwise.
     */
    public boolean isSelectedFlag() {
        return selectedFlag;
    }

    /**
     * Gets a player owning the piece.
     * @return A player.
     */
    public Player getPlayer() {
        return player;
    }
}
