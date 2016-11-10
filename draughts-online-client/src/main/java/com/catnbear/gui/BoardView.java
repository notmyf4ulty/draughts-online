package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class BoardView extends GridPane {
    GridPane boardGrid;
    BoardModel boardModel;

    public BoardView(BoardModel boardModel) {
        this.boardModel = boardModel;
        this.boardGrid = createBoard();
    }

    public GridPane createBoard() {
        final int FIELD_SIDE_LENGTH = 50;
        final String FIELD_COLOR_1_STYLE = "-fx-background-color: #c5925e;";
        final String FIELD_COLOR_2_STYLE = "-fx-background-color: #9f5000;";
        final String PIECE_COLOR_1_STYLE = "-fx-background-color: black;";
        final String PIECE_COLOR_2_STYLE = "-fx-background-color: white;";
        boardGrid = new GridPane();

        for (Field[] fields : boardModel.getFields()) {
            for (Field field : fields) {
                FieldPane fieldPane = new FieldPane();
                fieldPane.setPrefWidth(FIELD_SIDE_LENGTH);
                fieldPane.setPrefHeight(FIELD_SIDE_LENGTH);
                fieldPane.createPiece();
                fieldPane.setField(field);
                field.addObserver(fieldPane);
//                switch (field.getFieldColor()) {
//                    case COLOR_1:
//                        fieldPane.setStyle(FIELD_COLOR_1_STYLE);
//                        break;
//                    case COLOR_2:
//                        fieldPane.setStyle(FIELD_COLOR_2_STYLE);
//                        if (field.getPosition().getY() < 3) {
//                            Circle circle = new Circle(fieldPane.getPrefHeight() / 2 - 2);
//                            circle.setFill(Paint.valueOf("black"));
//                            circle.setVisible(true);
//                            fieldPane.getChildren().add(circle);
//                        } else if (field.getPosition().getY() > 4) {
//                            Circle circle = new Circle(fieldPane.getPrefHeight() / 2 - 2);
//                            circle.setFill(Paint.valueOf("white"));
//                            circle.setVisible(true);
//                            fieldPane.getChildren().add(circle);
//                        }
//                        break;
//                }
                boardGrid.add(fieldPane,field.getPosition().getX(),field.getPosition().getY());
            }
        }
        return boardGrid;
    }

    public GridPane getBoardGrid() {
        return boardGrid;
    }
}
