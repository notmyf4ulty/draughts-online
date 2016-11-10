package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class BoardView extends GridPane {
    GridPane boardGrid;
    BoardModel boardModel;

    public BoardView(BoardModel boardModel) {
        this.boardModel = boardModel;
        this.boardGrid = createBoard();
    }

    public GridPane createBoard() {
        final int FIELD_SIDE_LENGTH = 50;
        final String FIELD_COLOR_1_STYLE = "-fx-background-color: white;";
        final String FIELD_COLOR_2_STYLE = "-fx-background-color: gray;";
        boardGrid = new GridPane();

        for (Field[] fields : boardModel.getFields()) {
            for (Field field : fields) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefWidth(FIELD_SIDE_LENGTH);
                stackPane.setPrefHeight(FIELD_SIDE_LENGTH);
                switch (field.getFieldColor()) {
                    case COLOR_1:
                        stackPane.setStyle(FIELD_COLOR_1_STYLE);
                        break;
                    case COLOR_2:
                        stackPane.setStyle(FIELD_COLOR_2_STYLE);
                        break;
                }
                boardGrid.add(stackPane,field.getPosition().getX(),field.getPosition().getY());
            }
        }
        return boardGrid;
    }

    public GridPane getBoardGrid() {
        return boardGrid;
    }
}
