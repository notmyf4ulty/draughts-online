package com.catnbear;

/**
 * Server's side game's mode.
 */
class GameModel {
    /**
     * Singleton's instance.
     */
    private static GameModel instance;

    /**
     * Counter of joins.
     */
    volatile private int joinCounter;

    /**
     * String representation of the game board. Used for other thing also - for a win/loose communicates.
     */
    volatile private String board;

    /**
     * Flag indicating whether board is available.
     */
    private volatile boolean boardAvailableFlag;

    /**
     * Joined players' counter.
     */
    private volatile int playerCounter;

    /**
     * Unique new player's ID.
     */
    private volatile int playerId;

    /**
     * ID of the player who was the last board's updater.
     */
    private volatile int lastUpdatePlayerId;

    /**
     * Default constructor. It's private because of the Singletons purposes.
     */
    private GameModel() {}

    /**
     * Singleton's getInstance method.
     * @return Singleton's instance.
     */
    static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    /**
     * Getter of the joins counter.
     * @return Joins counter.
     */
    int getJoinCounter() {
        return joinCounter++;
    }

    /**
     * Getter of the board.
     * @return String representation of the board.
     */
    String getBoard() {
        boardAvailableFlag = false;
        return board;
    }

    /**
     * Setter of the board.
     * @param board New string representation of the board.
     */
    void setBoard(String board) {
        this.board = board;
    }

    /**
     * Getter of the boardAvailableFlag. It depends of the player who asks. Board cannot be available twice to the
     * same player in a row.
     * @param playerId ID of the asking player.
     * @return True if it is available. False otherwise.
     */
    boolean isBoardAvailable(int playerId) {
        return (lastUpdatePlayerId != playerId) && boardAvailableFlag;
    }

    /**
     * Adds a player.
     */
    void addPlayer() {
        if (++playerCounter > 2) {
            playerCounter = 1;
        }
    }

    /**
     * Checks if the game is ready, that is to say, there is proper players number.
     * @return True if the game is ready. False otherwise.
     */
    boolean isGameReady() {
        return playerCounter == 2;
    }

    /**
     * Getter of the unique player's ID.
     * @return New player's ID.
     */
    int getPlayerId() {
        return playerId++;
    }

    /**
     * Indicates are there two players.
     * @return True if there are two players. False otherwise.
     */
    public boolean areTwoPlayers() {
        return playerCounter % 2 == 0;
    }

    /**
     * Setter of the boardAvailableFlag.
     * @param updatePlayerId Player which updates the board.
     */
    void setBoardAvailableFlag(int updatePlayerId) {
        lastUpdatePlayerId = updatePlayerId;
        boardAvailableFlag = true;
    }
}
