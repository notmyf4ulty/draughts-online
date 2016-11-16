package com.catnbear.gui;

import com.catnbear.model.game.Board;
import com.catnbear.model.game.GameModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BoardWindowController {

    @FXML
    private VBox mainPane;

    @FXML
    private VBox boardPane;

    @FXML
    private Label activePlayerLabel;

    @FXML
    private Button endTurnButton;

    @FXML
    private Button resetTurnButton;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button startGameButton;

    @FXML
    private Label communicateLabel;

    private GameModel gameModel;
    private Board board;

    private Alert alertDialog;
    private CommunicateListener communicateListener;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        board = new Board();
        createBoard();
        gameModel.assignBoardModel(board);
//        gameModel.prepareNewRound();
        initialDisableGui();
        communicateListener = new CommunicateListener();
        gameModel.getGameStatus().addListener(communicateListener);
    }


    @FXML
    private void startGameButtonCallback() {
        gameModel.startNewGame();
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

    private void disableGui() {
        endTurnButton.setDisable(true);
        resetTurnButton.setDisable(true);
        surrenderButton.setDisable(true);
        startGameButton.setDisable(false);
    }

    private void initialDisableGui() {
        endTurnButton.setDisable(true);
        resetTurnButton.setDisable(true);
        surrenderButton.setDisable(true);
        startGameButton.setDisable(false);
    }

    private void enableGui() {
        endTurnButton.setDisable(true);
        resetTurnButton.setDisable(true);
        surrenderButton.setDisable(true);
        startGameButton.setDisable(true);
    }

    private void createBoard() {
        BoardView boardView = new BoardView(board);
        boardPane.getChildren().add(boardView);
    }

    private void gameDeinitialize() {
        gameModel.getGameStatus().removeListener(communicateListener);
    }

    private class CommunicateListener implements ChangeListener<Boolean> {
        @Override
        public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
            if (observableValue.getValue() == true) {
                switch (gameModel.getGameStatus().getStatusState()) {
                    case NOT_STARTED:
                        initNewGame();
                        break;
                    case CONNECTING_TO_SERVER:
                        connectToServer();
                        break;
                    case WAITING_FOR_SECOND_PLAYER:
                        waitForSecondPlayer();
                        break;
                    case WAITING_FOR_TURN:
                        waitForTurn();
                        break;
                    case TURN:
                        playerTurn();
                        break;
                    case LOST:
                        lost();
                        break;
                    case WON:
                        won();
                        break;
                    case CONNECTION_ERROR:
                        handleConnectionError();
                        break;
                }
            }
        }
    }

    private void initNewGame() {
        initialDisableGui();
        communicateLabel.setText("Click 'Start Game'");
    }

    private void connectToServer() {
        disableGui();
        communicateLabel.setText("Connecting to server...");
    }

    private void waitForSecondPlayer() {
        disableGui();
        communicateLabel.setText("Waiting for second player...");
    }

    private void waitForTurn() {
        disableGui();
        communicateLabel.setText("Wait for your turn.");
    }

    private void playerTurn() {
        enableGui();
        communicateLabel.setText("Your turn");
    }

    private void lost() {
        disableGui();
        communicateLabel.setText("You lost.");
    }

    private void won() {
        disableGui();
        communicateLabel.setText("You won.");
    }


    private void handleConnectionError() {
        disableGui();
        communicateLabel.setText("Connection error.");
    }
}
