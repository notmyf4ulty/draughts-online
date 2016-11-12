package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.GameModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BoardWindowController {

    @FXML
    private VBox boardPane;

    @FXML
    private Label activePlayerLabel;

    private GameModel gameModel;
    private BoardModel boardModel;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        boardModel = new BoardModel();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        createBoard();
    }

    private void createBoard() {
        BoardView boardView = new BoardView(boardModel);
        boardPane.getChildren().add(boardView);
    }

    @FXML
    private void endTurnButtonCallback() {
        gameModel.nextPlayer();
    }
}
