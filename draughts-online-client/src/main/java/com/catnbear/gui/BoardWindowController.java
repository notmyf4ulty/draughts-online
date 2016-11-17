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

    /**
     * Main window's pane.
     */
    @FXML
    private VBox mainPane;

    /**
     * Game board's pane.
     */
    @FXML
    private VBox boardPane;

    /**
     * Label showing given player's color.
     */
    @FXML
    private Label playerLabel;

    /**
     * Button for starting a game.
     */
    @FXML
    private Button startGameButton;

    /**
     * Button for ending a turn.
     */
    @FXML
    private Button endTurnButton;

    /**
     * Button for resetting a turn.
     */
    @FXML
    private Button resetTurnButton;

    /**
     * Button for surrendering.
     */
    @FXML
    private Button surrenderButton;

    /**
     * Button for exiting a game.
     */
    @FXML
    private Button exitGameButton;

    /**
     * Label for givin in-game communicates.
     */
    @FXML
    private Label communicateLabel;

    /**
     * Game's model instance.
     */
    private GameModel gameModel;

    /**
     * Game's board instance.
     */
    private Board board;

    /**
     * Window's initialization method.
     */
    @FXML
    private void initialize() {
        gameModel = GameModel.getInstance();
        playerLabel.textProperty().bind(gameModel.activePlayerLabelTextProperty());
        board = new Board();
        createBoard();
        gameModel.assignBoardModel(board);
        initialDisableButtons();
        CommunicateListener communicateListener = new CommunicateListener();
        gameModel.getGameStatus().addListener(communicateListener);
    }

    /**
     * Callback of startGameButton's action.
     */
    @FXML
    private void startGameButtonCallback() {
        disableAllButtons();
        gameModel.startNewGame();
    }

    /**
     * Callback of endTurnButton's action.
     */
    @FXML
    private void endTurnButtonCallback() {
        gameModel.prepareNewRound();
    }

    /**
     * Callback of resetTurnButton's action.
     */
    @FXML
    private void resetTurnButtonCallback() {
        gameModel.retreiveBackup();
    }

    /**
     * Callback of surrenderButton's action.
     */
    @FXML
    private void surrenderButtonCallback() {
        gameModel.surrender();
    }

    /**
     * Callback of exitButton's action.
     */
    @FXML
    private void exitButtonCallback() {
        gameModel.exit();
    }

    /**
     * Creates a board.
     */
    private void createBoard() {
        BoardPane boardPane = new BoardPane(board);
        this.boardPane.getChildren().add(boardPane);
    }

    /**
     * Class for tracking changes in game's model. Regarding to the game's status, appropriate action are taken.
     */
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

    /**
     * Customizes GUI when the game started.
     */
    private void initNewGame() {
        initialDisableButtons();
        communicateLabel.setText("Click 'Start Game'");
    }

    /**
     * Customizes GUI when connected to the server.
     */
    private void connectToServer() {
        disableAllButtons();
        communicateLabel.setText("Connecting to server...");
    }

    /**
     * Customizes GUI when waits for a second player.
     */
    private void waitForSecondPlayer() {
        waitForSecondPlayerDisableButtons();
        communicateLabel.setText("Waiting for second player...");
    }

    /**
     * Customizes GUI when waits for next turn.
     */
    private void waitForTurn() {
        waitForTurnDisableButtons();
        communicateLabel.setText("Wait for your turn.");
    }

    /**
     * Customizes GUI when it's player's turn.
     */
    private void playerTurn() {
        turnEnableButtons();
        communicateLabel.setText("Your turn");
    }

    /**
     * Customizes GUI when player lost the game.
     */
    private void lost() {
        disableAllButtons();
        showExitMessageDialog("You lost");
    }

    /**
     * Customizes GUI when player won the game.
     */
    private void won() {
        disableAllButtons();
        showExitMessageDialog("You won");
    }


    /**
     * Customizes GUI when connection error occurred.
     */
    private void handleConnectionError() {
        if (!gameModel.isConnectionErrorFlag()) {
            disableAllButtons();
            showExitMessageDialog("Connection error.");
        }
    }

    /**
     * Customizes GUI when exited the game.
     */
    private void handleExit() {
        showExitMessageDialog("Leaving a game.");
    }

    /**
     * Disables all the buttons.
     */
    private void disableAllButtons() {
        startGameButton.setDisable(true);
        endTurnButton.setDisable(true);
        resetTurnButton.setDisable(true);
        surrenderButton.setDisable(true);
        exitGameButton.setDisable(true);
    }

    /**
     * Disables buttons when game started.
     */
    private void initialDisableButtons() {
        disableAllButtons();
        startGameButton.setDisable(false);
    }

    /**
     * Disables buttons when waiting for a second player.
     */
    private void waitForSecondPlayerDisableButtons() {
        disableAllButtons();
        exitGameButton.setDisable(false);
    }

    /**
     * Disables buttons when waiting for a turn.
     */
    private void waitForTurnDisableButtons() {
        disableAllButtons();
        surrenderButton.setDisable(false);
    }

    /**
     * Enables all the buttons.
     */
    private void enableAllButtons() {
        startGameButton.setDisable(false);
        endTurnButton.setDisable(false);
        resetTurnButton.setDisable(false);
        surrenderButton.setDisable(false);
        exitGameButton.setDisable(false);
    }

    /**
     * Enables buttons when it's player's turn.
     */
    private void turnEnableButtons() {
        enableAllButtons();
        startGameButton.setDisable(true);
        exitGameButton.setDisable(true);
    }

    /**
     * Shows a modal message dialog, acknowledging of which finishes the game.
     * @param message Dialog's message.
     */
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
