package com.catnbear.model.game;

import java.util.*;

public class Board extends Observable {
    private static final int BOARD_DIMENSION = 8;
    private Field [][] board;
    private Field [][] boardBackup;
    private GameModel gameModel;

    public Board() {
        board = generateBoard();
        backupBoard();
        gameModel = GameModel.getInstance();
        gameModel.assignBoardModel(this);
    }

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

    private Field setInitialConfiguration(Position position) {
        Field field = new Field(position);
        if (position.isXySumEven()) {
            int playerOnePieceGenerationBorder = 3;
            int playerTwoPieceGenerationBorder = 4;
            if (position.getY() < playerOnePieceGenerationBorder) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.PLAYER_1);
                piece.assignField(field);
            } else if (position.getY() > playerTwoPieceGenerationBorder) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.PLAYER_2);
                piece.assignField(field);
            }
        }
        return field;
    }

    public void chooseField(Position position) {
        Field chosenField = board[position.getX()][position.getY()];
        if (gameModel.isMoveAvailable()) {
            Field activePieceField = getFieldOfActivePiece();
            if (activePieceField != null) {
                moveActivePiece(activePieceField, chosenField);
            }
            if (chosenField.containsPiece()) {
                selectPiece(chosenField);
            }
            setChanged();
            notifyObservers();
        }
    }

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

    private void moveActivePiece(Field fromField, Field toField) {
        if (!toField.containsPiece() &&
                isProperDirection(fromField.getPosition(),toField.getPosition())) {
            switch (Position.getMoveDistance(fromField.getPosition(),toField.getPosition())) {
                case ONE_FIELD_DISTANCE:
                    movePiece(fromField,toField);
                    break;
                case TWO_FIELDS_DISTANCE:
                    movePieceByTwoFields(fromField,toField);
                    break;
                case INCORRECT_DISTANCE:
                    break;
            }
        }
        resetSelection();
    }

    private boolean isProperDirection(Position fromPosition, Position toPosition) {
        int yFrom = fromPosition.getY();
        int yTo = toPosition.getY();
        switch (gameModel.getActivePlayer()) {
            case PLAYER_1:
                return yTo > yFrom;
            case PLAYER_2:
                return yTo < yFrom;
        }
        return false;
    }

    private void movePiece(Field fromField, Field toField) {
        Piece piece = fromField.getPiece();
        fromField.resetPiece();
        toField.setPiece(piece);
        gameModel.setMoveAvailable(false);
    }

    private void movePieceByTwoFields(Field fromField, Field toField) {
        Player activePlayer = gameModel.getActivePlayer();
        int x = (toField.getPosition().getX() + fromField.getPosition().getX()) / 2;
        int y = (toField.getPosition().getY() + fromField.getPosition().getY()) / 2;
        Field enemyField = board[x][y];
        if (enemyField.containsPiece() && !enemyField.getPiece().getPlayer().equals(activePlayer)) {
            enemyField.resetPiece();
            movePiece(fromField,toField);
        }
    }

    private void selectPiece(Field field) {
        resetSelection();
        Player activePlayer = gameModel.getActivePlayer();
        if (field.getPiece().getPlayer().equals(activePlayer)) {
            field.selectPiece();
        }
    }

    private void resetSelection() {
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece()) {
                    field.unselectPiece();
                }
            }
        }
    }

    private Field[][] getCopy(Field[][] fromBoard) {
        Field [][] copyBoard = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                copyBoard[i][j] = fromBoard[i][j].getCopy();
            }
        }
        return copyBoard;
    }

    void backupBoard() {
        boardBackup = getCopy(board);
    }

    void retrieveBackup() {
        board = getCopy(boardBackup);
        setChanged();
        notifyObservers();
    }

    public Field[][] getBoard() {
        return board;
    }

}
