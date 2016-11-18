package com.catnbear.model.game;

import java.util.Observable;

/**
 * Field's class. Can be placed on a board and can contain a Piece.
 */
public class Field extends Observable {

    /**
     * Contained Piece (can be a null).
     */
    private Piece piece;

    /**
     * Field's position.
     */
    private final Position position;

    /**
     * Constructor.
     * @param position Field's position.
     */
    Field(Position position) {
        this.piece = null;
        this.position = position;
    }

    /**
     * Checks whether field's piece is selected.
     * @return True if it is selected. False otherwise.
     */
    boolean isPieceSelected() {
        return piece.isSelectedFlag();
    }

    /**
     * Selects a piece.
     */
    void selectPiece() {
        if (piece != null) {
            piece.select();
        }
    }

    /**
     * Unselects a piece.
     */
    void unselectPiece() {
        if (piece != null) {
            piece.unselect();
        }
    }

    /**
     * Clears field's piece.
     */
    void clearPiece() {
        piece = null;
    }

    /**
     * Getter of field's copy.
     * @return Field's copy.
     */
    Field getCopy() {
        Field field = new Field(this.position.getCopy());
        if (containsPiece()) {
            Piece piece = this.piece.getCopy();
            piece.assignField(field);
        }
        return field;
    }

    /**
     * Getter of field's position.
     * @return Field's position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Checks whether field contains a piece.
     * @return True if it contains. False otherwise.
     */
    public boolean containsPiece() {
        return piece != null;
    }

    /**
     * Getter of the field's piece.
     * @return Field's piece.
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Setter of the field's piece.
     * @param piece New field's piece.
     */
    void setPiece(Piece piece) {
        this.piece = piece;
    }
}
