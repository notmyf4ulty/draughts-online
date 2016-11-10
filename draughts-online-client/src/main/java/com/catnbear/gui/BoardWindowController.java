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
    private GridPane boardGrid;
    BoardView boardView;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        boardModel = gameModel.getBoardModel();
        createBoard();
    }

    public void createBoard() {
        boardView = new BoardView(boardModel);
        boardGrid = boardView.getBoardGrid();
        mainPane.getChildren().add(boardGrid);
    }

    private void fillBoardWithDraught() {

    }
}
