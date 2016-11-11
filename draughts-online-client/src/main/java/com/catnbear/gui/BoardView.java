package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import javafx.scene.layout.GridPane;

import java.util.Iterator;
import java.util.Map;

public class BoardView extends GridPane {
    GridPane boardGrid;
    BoardModel boardModel;

    public BoardView(BoardModel boardModel) {
        this.boardModel = boardModel;
        this.boardGrid = createBoard();
    }

    public GridPane createBoard() {
        final int FIELD_SIDE_LENGTH = 50;
        boardGrid = new GridPane();
        Iterator iterator = boardModel.getFields().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Field field = (Field)pair.getValue();
            FieldPane fieldPane = new FieldPane();
            fieldPane.setPrefWidth(FIELD_SIDE_LENGTH);
            fieldPane.setPrefHeight(FIELD_SIDE_LENGTH);
            fieldPane.bindField(field);
            boardGrid.add(fieldPane, field.getPosition().getX(), field.getPosition().getY());
            iterator.remove();
        }
        return boardGrid;
    }

    public GridPane getBoardGrid() {
        return boardGrid;
    }
}
