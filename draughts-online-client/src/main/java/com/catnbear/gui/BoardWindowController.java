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
    BoardView boardView;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        boardModel = gameModel.getBoardModel();
        createBoard();
    }

    private void createBoard() {
        boardView = new BoardView(boardModel);
        mainPane.getChildren().add(boardView);
    }
}
