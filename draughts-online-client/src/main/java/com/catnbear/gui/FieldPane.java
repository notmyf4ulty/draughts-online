package com.catnbear.gui;

import com.catnbear.model.game.Field;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import java.util.Observable;
import java.util.Observer;

public class FieldPane extends StackPane implements Observer {
    final String FIELD_COLOR_1_STYLE = "-fx-background-color: #c5925e;";
    final String FIELD_COLOR_2_STYLE = "-fx-background-color: #9f5000;";
    final String PIECE_COLOR_1_STYLE = "black";
    final String PIECE_COLOR_2_STYLE = "white";

    Field field;
    Circle piece;

    FieldPane() {
        super();
    }

    @Override
    public void update(Observable observable, Object o) {
        if(field.containsPiece()) {
            createPiece();
        } else {
            deletePiece();
        }
    }

    private void createPiece() {
        if (piece == null) {
            piece = new Circle(this.getPrefHeight() / 2 - 2);
            piece.setOnMouseClicked(mouseEvent -> {

            });
            this.getChildren().add(piece);
            setPieceColor();
        }
    }

    private void deletePiece() {
        piece = null;
    }

    private void setPieceColor() {
        if (piece != null) {
            piece.setVisible(true);
            switch (field.getPiece().getOwner()) {
                case PLAYER_1:
                    piece.setFill(Paint.valueOf(PIECE_COLOR_1_STYLE));
                    break;
                case PLAYER_2:
                    piece.setFill(Paint.valueOf(PIECE_COLOR_2_STYLE));
                    break;
                case NONE:
                    piece.setVisible(false);
                    break;
            }
        }
    }

    void bindField(Field field) {
        this.field = field;
        this.field.addObserver(this);
        update(this.field,null);
        setColor();
    }

    private void setColor() {
        if (field != null) {
            switch (field.getFieldColor()) {
                case COLOR_1:
                    setStyle(FIELD_COLOR_1_STYLE);
                    break;
                case COLOR_2:
                    setStyle(FIELD_COLOR_2_STYLE);
                    break;
            }
        }
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
