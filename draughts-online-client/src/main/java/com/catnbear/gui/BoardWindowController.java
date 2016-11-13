package com.catnbear.gui;

import com.catnbear.model.game.Board;
import com.catnbear.model.game.GameModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

public class BoardWindowController {

    @FXML
    private HBox mainPane;

    @FXML
    private VBox boardPane;

    @FXML
    private Label activePlayerLabel;

    @FXML
    private Label roundLabel;

    private GameModel gameModel;
    private Board board;

    @FXML
    private void initialize() {
        board = new Board();
        gameModel = GameModel.getInstance();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        roundLabel.textProperty().bind(gameModel.roundLabelValueProperty().asString());
        createBoard();
    }

    private void createBoard() {
        BoardView boardView = new BoardView(board);
        boardPane.getChildren().add(boardView);
    }

    @FXML
    private void endTurnButtonCallback() {
        gameModel.prepareNewRound();
    }

    @FXML
    private void resetTurnButtonCallback() {
        gameModel.retreiveBackup();
    }
}
