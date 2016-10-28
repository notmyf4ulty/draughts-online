package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import javafx.fxml.FXML;
import javafx.scene.layout.*;

import java.util.List;

public class BoardWindowController {

    @FXML
    private VBox mainPane;

    private BoardModel boardModel;
    private GridPane board;

    @FXML
    private void initialize() {
        boardModel = BoardModel.getInstance();
        generateBoard();
    }

    public void generateBoard() {
        final int FIELD_SIDE_LENGTH = 50;
        final String FIELD_COLOR_1_STYLE = "-fx-background-color: white;";
        final String FIELD_COLOR_2_STYLE = "-fx-background-color: gray;";
        board = new GridPane();

        boardModel.getBoard();

        for (List<Field> fields : boardModel.getBoard()) {
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
                board.add(stackPane,field.getPosition().getX(),field.getPosition().getY());
            }
        }
        mainPane.getChildren().add(board);
    }

    private void fillBoardWithDraught() {

    }
}
