package com.catnbear.model.game;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by przemek on 28.10.16.
 */
public class Field extends Observable {

    public enum FieldColor {
        COLOR_1,
        COLOR_2;
    }

    private final FieldColor fieldColor;
    private Piece piece;
    private final Position position;

    public Field(FieldColor fieldColor, Position position) {
        this.fieldColor = fieldColor;
        this.piece = null;
        this.position = position;
    }

    public FieldColor getFieldColor() {
        return fieldColor;
    }

    public Position getPosition() {
        return position;
    }
}
