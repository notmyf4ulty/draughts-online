package com.catnbear.model.game;

import com.catnbear.communication.Connection;
import com.catnbear.communication.ConnectionParameters;
import javafx.application.Platform;
import javafx.beans.property.*;

public class GameModel {
    /**
     * Singleton's instance.
     */
    private static GameModel instance = null;

    /**
     * Chosen player (white/black).
     */
    private Player player;

    /**
     * String property containing a player.
     */
    private StringProperty playerLabelText;

    /**
     * Flag indicating if there is a move available.
     */
    private boolean moveAvailableFlag;

    /**
     * Board's instance.
     */
    private Board board;

    /**
     * Servers connection.
     */
    private Connection connection;

    /**
     * Status of a game.
     */
    private GameStatus gameStatus;

    /**
     * Flag indicating if there was an connection error.
     */
    private boolean connectionErrorFlag;

    /**
     * Default constructor. It's private because of the Singleton design pattern's purposes.
     */
    private GameModel() {
        gameStatus = new GameStatus();
        playerLabelText = new SimpleStringProperty("");
    }

    /**
     * Getter of the Singleton's instance.
     * @return Class' instance.
     */
    public static GameModel getInstance() {
        if(instance == null) {
            System.out.println("Starting new game.");
            instance = new GameModel();
        }
        return instance;
    }

    /**
     * Getter of chosen player.
     * @return A player.
     */
    Player getPlayer() {
        return player;
    }

    /**
     * Actions taken after the new game's start.
     */
    public void startNewGame() {
        gameStatus.setStatusState(GameStatus.StatusState.CONNECTING_TO_SERVER);
        if (establishConnection()) {
            joinGame();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    /**
     * Establishes a connection with a server.
     * @return True if connection was established. False otherwise.
     */
    private boolean establishConnection() {
        ConnectionParameters connectionParameters = ConnectionParameters.getInstance();
        connection = Connection.getInstance();
        initializeConnectionHandlers();
        connection.setConnectionParameters(connectionParameters.getHostName(), connectionParameters.getPortNumber());
        return connection.connect();
    }

    /**
     * Joins a game.
     */
    private void joinGame() {
        gameStatus.setStatusState(GameStatus.StatusState.JOINING_GAME);
        connection.sendData("join");
        connection.waitForData();
    }

    /**
     * Handler for a available data.
     */
    private void handleDataReady() {
        String receivedData = connection.getData();
        if (receivedData != null) {
            System.out.println("Handling received data: " + receivedData);
            switch (gameStatus.getStatusState()) {
                case CONNECTING_TO_SERVER:
                    break;
                case JOINING_GAME:
                    gameJoined(receivedData);
                    break;
                case WAITING_FOR_SECOND_PLAYER:
                    secondPlayerReady(receivedData);
                    break;
                case WAITING_FOR_TURN:
                    newRound(receivedData);
                    break;
            }
        }
    }

    /**
     * Action taken after joining a game.
     * @param playerString String with player choice given by a server.
     */
    private void gameJoined(String playerString) {
        switch (playerString) {
            case "w":
                player = Player.WHITE;
                break;
            case "b":
                player = Player.BLACK;
                break;
            default:
                player = Player.INCORRECT_PLAYER;
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                break;
        }
        if (!player.equals(Player.INCORRECT_PLAYER)) {
            Platform.runLater(() -> playerLabelText.setValue(player.toString()));
            waitForNextPlayer();
        }
    }

    /**
     * Action taken when waiting for a next player.
     */
    private void waitForNextPlayer() {
        gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_SECOND_PLAYER);
        connection.sendData("startready");
        connection.waitForData();
    }

    /**
     * Action taken after a second player is ready.
     * @param gameReadyMessage Message indicating that the game is ready.
     */
    private void secondPlayerReady(String gameReadyMessage) {
        if (gameReadyMessage.equals("gameready")) {
            startGame();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
        }
    }

    /**
     * Starts a game.
     */
    private void startGame() {
        moveAvailableFlag = true;
        switch (player) {
            case WHITE:
                gameStatus.setStatusState(GameStatus.StatusState.TURN);
                break;
            case BLACK:
                gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_TURN);
                waitForFirstRound();
                break;
            case INCORRECT_PLAYER:
                gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                break;
        }
    }

    /**
     * Waits for a first round.
     */
    private void waitForFirstRound() {
        connection.sendData("turnready");
        connection.waitForData();
    }

    /**
     * Prepares a new round.
     */
    public void prepareNewRound() {
        if (!moveAvailableFlag) {
            backupBoardModel();
            endTurn();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.TURN);
        }
    }

    /**
     * Action taken after the end of a turn.
     */
    private void endTurn() {
        int enemyPiecesNumber = board.countEnemyPieces();
        if (enemyPiecesNumber != 0) {
            gameStatus.setStatusState(GameStatus.StatusState.WAITING_FOR_TURN);
            connection.sendData(board.prepareToSend());
            connection.sendData("turnready");
            connection.waitForData();
        } else {
            gameStatus.setStatusState(GameStatus.StatusState.WON);
            connection.sendData("won");
        }
    }

    /**
     * Backups board's model.
     */
    private void backupBoardModel() {
        if (board != null) {
            board.backupBoard();
        }
    }

    /**
     * Retrieves a board's backup.
     */
    public void retrieveBackup() {
        if (board != null) {
            board.retrieveBackup();
        }
        moveAvailableFlag = true;
    }

    /**
     * Initializes connection handlers. Currently there is only one handler.
     */
    private void initializeConnectionHandlers() {
        connection.addDataReadyListener((observableValue, aBoolean, t1) -> handleDataReady());
    }

    /**
     * Action taken after the new round started.
     * @param boardString Board's string representation.
     */
    private void newRound(String boardString) {
        Platform.runLater(() -> {
            switch (boardString) {
                case "lost":
                    gameStatus.setStatusState(GameStatus.StatusState.LOST);
                    break;
                case "won":
                    gameStatus.setStatusState(GameStatus.StatusState.WON);
                    break;
                default:
                    if (board.createBoardFromString(boardString)) {
                        gameStatus.setStatusState(GameStatus.StatusState.TURN);
                        moveAvailableFlag = true;
                    } else {
                        gameStatus.setStatusState(GameStatus.StatusState.CONNECTION_ERROR);
                    }
                    break;
            }
        });
    }

    /**
     * Action taken after the player's surrender.
     */
    public void surrender() {
        gameStatus.setStatusState(GameStatus.StatusState.LOST);
        connection.sendData("lost");
    }

    /**
     * Action taken after the game's exit.
     */
    public void exit() {
        gameStatus.setStatusState(GameStatus.StatusState.EXIT);
    }

    /**
     * Assigns a board's model.
     * @param board Board's model.
     */
    public void assignBoardModel(Board board) {
        this.board = board;
    }

    /**
     * Getter of moveAvailableFlag.
     * @return A moveAvailableFlag.
     */
    boolean isMoveAvailableFlag() {
        return moveAvailableFlag;
    }

    /**
     * Setter of moveAvailableFlag.
     * @param moveAvailableFlag New moveAvailableFlag.
     */
    void setMoveAvailableFlag(boolean moveAvailableFlag) {
        this.moveAvailableFlag = moveAvailableFlag;
    }

    /**
     * Getter of playerLabelTextProperty.
     * @return a playerLabelTextProperty.
     */
    public StringProperty playerLabelTextProperty() {
        return playerLabelText;
    }

    /**
     * Getter of gameStatus.
     * @return A game's status.
     */
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Getter of connectionErrorFlag.
     * @return A connectionErrorFlag.
     */
    public boolean isConnectionErrorFlag() {
        boolean value = connectionErrorFlag;
        connectionErrorFlag = true;
        return value;
    }

    /**
     * Fully resets a game's model.
     */
    public void resetGameModel() {
        instance = null;
        player = null;
        playerLabelText = null;
        moveAvailableFlag = false;
        board = null;
        connection.resetConnection();
        gameStatus = null;
    }
}
