package com.catnbear.gui;

import com.catnbear.model.game.Board;
import com.catnbear.model.game.GameModel;
import com.catnbear.utilities.GuiModifier;
import javafx.application.Platform;
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
    private Button startGameButton;

    @FXML
    private Button endTurnButton;

    @FXML
    private Button resetTurnButton;

    @FXML
    private Button surrenderButton;

    @FXML
    private Button exitGameButton;

    @FXML
    private Label communicateLabel;

    private GameModel gameModel;

    private Board board;

    private CommunicateListener communicateListener;

    private boolean connectionErrorFlag;

    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        activePlayerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        board = new Board();
        createBoard();
        gameModel.assignBoardModel(board);
        initialDisableGui();
        communicateListener = new CommunicateListener();
        gameModel.getGameStatus().addListener(communicateListener);
    }

    @FXML
    private void startGameButtonCallback() {
        disableGui();
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

    @FXML
    private void exitButtonCallback() {
        gameModel.exit();
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
            if (t1) {
                System.out.println("Status changed: " + gameModel.getGameStatus().getStatusState());
                Platform.runLater(() -> {
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
                        case EXIT:
                            handleExit();
                            break;
                    }
                });
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
        waitForSecondPlayerDisableGui();
        communicateLabel.setText("Waiting for second player...");
    }

    private void waitForTurn() {
        waitForTurnDisableGui();
        communicateLabel.setText("Wait for your turn.");
    }

    private void playerTurn() {
        turnEnableGui();
        communicateLabel.setText("Your turn");
    }

    private void lost() {
        disableGui();
        showExitMessageDialog("You lost");
    }

    private void won() {
        disableGui();
        showExitMessageDialog("You won");
    }


    private void handleConnectionError() {
        if (!gameModel.isConnectionErrorFlag()) {
            disableGui();
            showExitMessageDialog("Connection error.");
        }
    }

    private void handleExit() {
        showExitMessageDialog("Leaving a game.");
    }

    private void disableGui() {
        startGameButton.setDisable(true);
        endTurnButton.setDisable(true);
        resetTurnButton.setDisable(true);
        surrenderButton.setDisable(true);
        exitGameButton.setDisable(true);
    }

    private void initialDisableGui() {
        disableGui();
        startGameButton.setDisable(false);
    }

    private void waitForSecondPlayerDisableGui() {
        disableGui();
        exitGameButton.setDisable(false);
    }

    private void waitForTurnDisableGui() {
        disableGui();
        surrenderButton.setDisable(false);
    }

    private void enableGui() {
        startGameButton.setDisable(false);
        endTurnButton.setDisable(false);
        resetTurnButton.setDisable(false);
        surrenderButton.setDisable(false);
        exitGameButton.setDisable(false);
    }

    private void turnEnableGui() {
        enableGui();
        startGameButton.setDisable(true);
        exitGameButton.setDisable(true);
    }

    private void showExitMessageDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnCloseRequest(dialogEvent -> {
            gameModel.resetGameModel();
            GuiModifier.changeWindow(mainPane,"/gui/welcomescreen.fxml",this);
        });
        alert.showAndWait();
    }
}
