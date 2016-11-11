package com.catnbear.gui;

import com.catnbear.model.game.Field;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Observable;
import java.util.Observer;

public class FieldPane extends StackPane implements Observer {
    Field field;
    Circle piece;

    public FieldPane() {
        super();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(field.containsPiece()) {
            createPiece();
            setPieceColor();
        }
    }

    public void createPiece() {
        if (piece == null) {
            piece = new Circle(this.getPrefHeight() / 2 - 2);
            this.getChildren().add(piece);
        }
    }

    private void setPieceColor() {
        if (piece != null) {
            piece.setVisible(true);
            switch (field.getPiece().getOwner()) {
                case PLAYER_1:
                    piece.setFill(Paint.valueOf("black"));
                    break;
                case PLAYER_2:
                    piece.setFill(Paint.valueOf("white"));
                    break;
                case NONE:
                    piece.setVisible(false);
                    break;
            }
        }
    }

    public void bindField(Field field) {
        this.field = field;
        this.field.addObserver(this);
        update(this.field,null);
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
        if(field.containsPiece()) {
            setPieceColor();
        }
    }
}
