package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import javafx.scene.layout.GridPane;

import java.util.Iterator;
import java.util.Map;

public class BoardView extends GridPane {
    BoardModel boardModel;

    public BoardView(BoardModel boardModel) {
        this.boardModel = boardModel;
        createBoard();
    }

    private void createBoard() {
        final int FIELD_SIDE_LENGTH = 50;
        Iterator iterator = boardModel.getFields().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Field field = (Field)pair.getValue();
            FieldView fieldView = new FieldView();
            fieldView.setPrefWidth(FIELD_SIDE_LENGTH);
            fieldView.setPrefHeight(FIELD_SIDE_LENGTH);
            fieldView.bindField(field);
            add(fieldView, field.getPosition().getX(), field.getPosition().getY());
            iterator.remove();
        }
    }
}
