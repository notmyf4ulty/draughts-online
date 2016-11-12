package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.GameModel;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BoardWindowController {

    @FXML
    private VBox boardPane;

    @FXML
    private Label activePlayerLabel;

    @FXML
    private Label roundLabel;

    private GameModel gameModel;
    private BoardModel boardModel;

    @FXML
    private void initialize() {
        boardModel = new BoardModel();
        gameModel = GameModel.getInstance();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        roundLabel.textProperty().bind(gameModel.roundLabelValueProperty().asString());
        createBoard();
    }

    private void createBoard() {
        BoardView boardView = new BoardView(boardModel);
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
