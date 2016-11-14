package com.catnbear.gui;

import com.catnbear.model.game.Board;
import com.catnbear.model.game.GameModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BoardWindowController {

    @FXML
    private HBox mainPane;

    @FXML
    private VBox boardPane;

    @FXML
    private Label activePlayerLabel;

    private GameModel gameModel;
    private Board board;

    private Alert alertDialog;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        gameModel.communicateLabelTextProperty().addListener(new CommunicateListener());
        board = new Board();
        createBoard();
        gameModel.assignBoardModel(board);
        gameModel.prepareNewRound();
    }


    private void createBoard() {
        BoardView boardView = new BoardView(board);
        boardPane.getChildren().add(boardView);
    }

    private class CommunicateListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String value = observableValue.getValue();
            switch (value) {
                case "startwait":
                    alertDialog = new Alert(Alert.AlertType.NONE);
                    alertDialog.setContentText("You are second in queue.\nWait for your round.");
                    alertDialog.show();
                    break;
                case "start":
                    if (alertDialog != null) {
                        alertDialog.close();
                    }
                    break;
                case "lost":
                    break;
                case "win":
                    break;
                case "connlost":
                    break;
            }
        }
    }

    @FXML
    private void endTurnButtonCallback() {
        gameModel.prepareNewRound();
    }

    @FXML
    private void resetTurnButtonCallback() {
        gameModel.retreiveBackup();
    }

    @FXML
    private void surrenderButtonCallback() {
        gameModel.surrender();
    }


}
