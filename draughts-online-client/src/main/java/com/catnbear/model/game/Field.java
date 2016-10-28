package com.catnbear.model.game;

/**
 * Created by przemek on 28.10.16.
 */
public class Field {

    public enum FieldColor {
        COLOR_1,
        COLOR_2;
    }

    private final FieldColor fieldColor;
    private boolean occupied;
    private final Position position;

    public Field(FieldColor fieldColor, Position position) {
        this.fieldColor = fieldColor;
        this.occupied = false;
        this.position = position;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public FieldColor getFieldColor() {
        return fieldColor;
    }

    public Position getPosition() {
        return position;
    }
}
