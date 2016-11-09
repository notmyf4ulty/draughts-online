package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.GameModel;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class BoardWindowController {

    @FXML
    private VBox mainPane;

    private GameModel gameModel;
    private BoardModel boardModel;
    private GridPane board;
    BoardView boardView;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        boardModel = gameModel.getBoardModel();
        createBoard();
    }

    public void createBoard() {
//        final int FIELD_SIDE_LENGTH = 50;
//        final String FIELD_COLOR_1_STYLE = "-fx-background-color: white;";
//        final String FIELD_COLOR_2_STYLE = "-fx-background-color: gray;";
//        board = new GridPane();
//
//        gameModel.getBoardModel();
//
//        for (Field [] fields : gameModel.getBoardModel().getFields()) {
//            for (Field field : fields) {
//                StackPane stackPane = new StackPane();
//                stackPane.setPrefWidth(FIELD_SIDE_LENGTH);
//                stackPane.setPrefHeight(FIELD_SIDE_LENGTH);
//                switch (field.getFieldColor()) {
//                    case COLOR_1:
//                        stackPane.setStyle(FIELD_COLOR_1_STYLE);
//                        break;
//                    case COLOR_2:
//                        stackPane.setStyle(FIELD_COLOR_2_STYLE);
//                        break;
//                }
//                board.add(stackPane,field.getPosition().getX(),field.getPosition().getY());
//            }
//        }
//        mainPane.getChildren().add(board);
        boardView = new BoardView(boardModel,board);
        board = boardView.getBoardGrid();
        mainPane.getChildren().add(board);
    }

    private void fillBoardWithDraught() {

    }
}
