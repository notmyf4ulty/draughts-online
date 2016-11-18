package com.catnbear.model.game;

import java.util.*;

/**
 * Logical game board's model. Extends Observable class, thus its changes can update the behavior of the Observers.
 */
public class Board extends Observable {
    /**
     * Board's dimension.
     */
    private static final int BOARD_DIMENSION = 5;

    /**
     * Actual board. An 2D array containing Field objects.
     */
    private Field [][] board;

    /**
     * Backup board.
     */
    private Field [][] boardBackup;

    /**
     * Game's model.
     */
    private GameModel gameModel;

    /**
     * Flag indicating multi - beating mode.
     */
    private boolean multiBeatModeFlag;

    /**
     * Actual field containing piece which can beat next piece.
     */
    private Field multiBeatingPieceField;

    /**
     * Class' constructor.
     */
    public Board() {
        board = generateBoard();
        backupBoard();
        gameModel = GameModel.getInstance();
        multiBeatModeFlag = false;
    }

    /**
     * Generates board.
     * @return Generated board.
     */
    private Field [][] generateBoard() {
        Field [][] board = new Field [BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                Position position = new Position(i,j);
                Field field = setInitialConfiguration(position);
                board[i][j] = field;
            }
        }
        return board;
    }

    /**
     * Sets initial field's configuration.
     * @param position Field's position.
     * @return Initially configured field.
     */
    private Field setInitialConfiguration(Position position) {
        Field field = new Field(position);
        if (position.isXySumEven()) {
            int playerOnePieceGenerationBorder = 2;
            int playerTwoPieceGenerationBorder = 2;
            if (position.getY() < playerOnePieceGenerationBorder) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.WHITE);
                piece.assignField(field);
            } else if (position.getY() > playerTwoPieceGenerationBorder) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.BLACK);
                piece.assignField(field);
            }
        }
        return field;
    }

    /**
     * Chooses a field of a piece which will be intendet to move.
     * @param position Field's position.
     */
    public void chooseField(Position position) {
        Field chosenField = board[position.getX()][position.getY()];
        if (gameModel.isMoveAvailableFlag()) {
            Field activePieceField;
            if (multiBeatModeFlag) {
                activePieceField = multiBeatingPieceField;
            } else {
                activePieceField = getFieldOfActivePiece();
            }

            if (activePieceField != null) {
                moveActivePiece(activePieceField, chosenField);
            }
            if (chosenField.containsPiece()) {
                if (!multiBeatModeFlag) {
                    selectPiece(chosenField);
                }
            }
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Gets field of actively selected piece.
     * @return Field if there is an active one. Null if there is no such Field.
     */
    private Field getFieldOfActivePiece() {
        for(Field [] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece() && field.isPieceSelcted()) {
                    return field;
                }
            }
        }
        return null;
    }

    /**
     * Moves actively selected piece.
     * @param fromField Start field.
     * @param toField Stop field.
     */
    private void moveActivePiece(Field fromField, Field toField) {
        if (!toField.containsPiece() &&
                isProperDirection(fromField.getPosition(),toField.getPosition())) {
            switch (Position.getMoveDistance(fromField.getPosition(),toField.getPosition())) {
                case ONE_FIELD_DISTANCE:
                    if (!multiBeatModeFlag) {
                        movePiece(fromField, toField);
                    }
                    break;
                case TWO_FIELDS_DISTANCE:
                    movePieceByTwoFields(fromField,toField);
                    break;
                case INCORRECT_DISTANCE:
                    break;
            }
        }
        if (!multiBeatModeFlag) {
            resetSelection();
        }
    }

    /**
     * Checks if piece is moved in a proper direction.
     * @param fromPosition Start position.
     * @param toPosition Stop position.
     * @return True if direction is proper. False otherwise.
     */
    private boolean isProperDirection(Position fromPosition, Position toPosition) {
        if(Position.getMoveDistance(fromPosition,toPosition).equals(MoveDistance.ONE_FIELD_DISTANCE)) {
            int yFrom = fromPosition.getY();
            int yTo = toPosition.getY();
            switch (gameModel.getPlayer()) {
                case WHITE:
                    return yTo > yFrom;
                case BLACK:
                    return yTo < yFrom;
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Moves piece.
     * @param fromField Start field.
     * @param toField Stop field.
     */
    private void movePiece(Field fromField, Field toField) {
        Piece piece = fromField.getPiece();
        fromField.resetPiece();
        toField.setPiece(piece);
        gameModel.setMoveAvailableFlag(false);
    }

    /**
     * Moves Piece by two fields - possible only when beating enemy's piece.
     * @param fromField Start field.
     * @param toField Stop field.
     */
    private void movePieceByTwoFields(Field fromField, Field toField) {
        Player activePlayer = gameModel.getPlayer();
        int x = (toField.getPosition().getX() + fromField.getPosition().getX()) / 2;
        int y = (toField.getPosition().getY() + fromField.getPosition().getY()) / 2;
        Field enemyField = board[x][y];
        if (enemyField.containsPiece() &&
                !enemyField.getPiece().getPlayer().equals(activePlayer)) {
            enemyField.resetPiece();
            movePiece(fromField,toField);
            multiBeatModeFlag = isOpponentToBeatAround(toField);
            if (multiBeatModeFlag) {
                gameModel.setMoveAvailableFlag(true);
            }
        }
    }

    /**
     * Checks if there is an opponent to beat.
     * @param field Actual field.
     * @return True if there is an opponent to beat. False otherwise.
     */
    private boolean isOpponentToBeatAround(Field field) {
        if (isPlaceBehindOpponents(field)) {
            multiBeatingPieceField = field;
            return true;
        } else {
            multiBeatingPieceField = null;
        }
        return false;
    }

    /**
     * Checks if there is a place behind a start field and an opponent's field.
     * @param attackingField Field to go.
     * @return True if there is a place. False otherwise.
     */
    private boolean isPlaceBehindOpponents(Field attackingField) {
        Position [] neighbouringPositions =
                attackingField
                .getPosition()
                .getDiagonalNeighboursPositions(2,BOARD_DIMENSION);
        for (Position position : neighbouringPositions) {
            Field behindOpponentField = board[position.getX()][position.getY()];
            if (!behindOpponentField.containsPiece() &&
                    Position
                            .getMoveDistance(attackingField.getPosition(),behindOpponentField.getPosition())
                            .equals(MoveDistance.TWO_FIELDS_DISTANCE) &&
                    isOpponentInTheMiddle(attackingField.getPosition(),behindOpponentField.getPosition())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is an opponent in the middle of the actual piece position and to-go position.
     * @param fromPosition Start position.
     * @param toPosition Stop position.
     * @return True if there is an opponent. False otherwise.
     */
    private boolean isOpponentInTheMiddle(Position fromPosition, Position toPosition) {
        int opponentX = (fromPosition.getX() + toPosition.getX()) / 2;
        int opponentY = (fromPosition.getY() + toPosition.getY()) / 2;
        if (opponentX != 0 && opponentY != 0) {
            Field opponentField = board[opponentX][opponentY];
            if (opponentField.containsPiece()) {
                return !opponentField.getPiece().getPlayer().equals(gameModel.getPlayer());
            }
        }
        return false;
    }

    /**
     * Selects a piece of a given field.
     * @param field Field containing a piece.
     */
    private void selectPiece(Field field) {
        resetSelection();
        Player activePlayer = gameModel.getPlayer();
        if (field.getPiece().getPlayer().equals(activePlayer)) {
            field.selectPiece();
        }
    }

    /**
     * Resets current selection.
     */
    private void resetSelection() {
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece()) {
                    field.unselectPiece();
                }
            }
        }
    }

    /**
     * Gets a copy of a given board's array.
     * @param fromBoard Board to be copied.
     * @return Copy of the given board.
     */
    private Field[][] getCopy(Field[][] fromBoard) {
        Field [][] copyBoard = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                copyBoard[i][j] = fromBoard[i][j].getCopy();
            }
        }
        return copyBoard;
    }

    /**
     * Creates board's backup.
     */
    void backupBoard() {
        boardBackup = getCopy(board);
    }

    /**
     * Retrieves backup.
     */
    void retrieveBackup() {
        board = getCopy(boardBackup);
        multiBeatModeFlag = false;
        multiBeatingPieceField = null;
        setChanged();
        notifyObservers();
    }

    /**
     * Prepares a board to send.
     * @return String form af a board which will be send to the server.
     */
    String prepareToSend() {
        String boardString = "";
        for (Field [] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece()) {
                    switch (field.getPiece().getPlayer()) {
                        case WHITE:
                            boardString += "w";
                            break;
                        case BLACK:
                            boardString += "b";
                            break;
                    }
                } else {
                    boardString += "o";
                }
            }
            boardString += ";";
        }
        return boardString;
    }

    /**
     * Creates a board from a message.
     * @param boardString String board's representation.
     * @return True if creation process was finished with success. False otherwise.
     */
    boolean createBoardFromString(String boardString) {
        try {
            System.out.println("Creating board from String: " + boardString);
            String[] boardColumns = boardString.split(";");
            Field[][] newBoard = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
            for (int i = 0; i < BOARD_DIMENSION; i++) {
                for (int j = 0; j < BOARD_DIMENSION; j++) {
                    Field field = new Field(new Position(i, j));
                    switch (boardColumns[i].charAt(j)) {
                        case 'w':
                            Piece whitePiece = new Piece(Piece.PieceType.MEN, Player.WHITE);
                            field.setPiece(whitePiece);
                            break;
                        case 'b':
                            Piece blackPiece = new Piece(Piece.PieceType.MEN, Player.BLACK);
                            field.setPiece(blackPiece);
                            break;
                    }
                    newBoard[i][j] = field;
                }
            }
            boardBackup = getCopy(newBoard);
            retrieveBackup();
            System.out.println("New board is: " + prepareToSend());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Counts enemy pieces on a board.
     * @return Number of enemy pieces.
     */
    int countEnemyPieces() {
        int counter = 0;
        for (int i = 0; i < BOARD_DIMENSION; i++) {
            for (int j = 0; j < BOARD_DIMENSION; j++) {
                Field field = board[i][j];
                if (field.containsPiece() && !field.getPiece().getPlayer().equals(gameModel.getPlayer())) {
                    counter++;
                }
            }
        }
        System.out.println("Returning " + counter);
        return counter;
    }

    /**
     * Getter of a board.
     * @return A board.
     */
    public Field[][] getBoard() {
        return board;
    }
}
