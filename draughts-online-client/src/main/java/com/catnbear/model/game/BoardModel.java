package com.catnbear.model.game;

import java.util.*;

public class BoardModel extends Observable {
    private static final int BOARD_DIMENSION = 8;
    private Field [][] board;
    private Field [][] boardBackup;
    private GameModel gameModel;

    public BoardModel() {
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
            if (position.getY() < 3) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.PLAYER_1);
                piece.assignField(field);
            } else if (position.getY() > 4) {
                Piece piece = new Piece(Piece.PieceType.MEN, Player.PLAYER_2);
                piece.assignField(field);
            }
        }
        return field;
    }

    public void clickField(Position position) {
        Player activePlayer = gameModel.getActivePlayer();
        Field selectedField = board[position.getX()][position.getY()];
        Field activePieceField = getFieldOfActivePiece();
        if (activePieceField != null) {
            moveActivePiece(activePieceField,selectedField);
            resetSelection();
        } else if (selectedField.containsPiece() && selectedField.getPiece().getPlayer().equals(activePlayer)) {
            resetSelection();
            selectedField.selectPiece();
        }
        setChanged();
        notifyObservers();
    }


    private void moveActivePiece(Field fromField, Field toField) {
        boolean diagonalOneFieldNeighbourhoodCondition =
                fromField.getPosition().isOneFieldDiagonalNeighbour(toField.getPosition());
        boolean noPieceOnToFieldCondition = !toField.containsPiece();
        boolean isProperDirectionCondition = isProperDirection(fromField,toField);

        if (diagonalOneFieldNeighbourhoodCondition &&
                noPieceOnToFieldCondition &&
                isProperDirectionCondition) {
            Piece piece = fromField.getPiece();
            fromField.resetPiece();
            toField.setPiece(piece);
        } else {
            if (canBeatEnemy(fromField, toField)) {
                int x = (toField.getPosition().getX() + fromField.getPosition().getX()) / 2;
                int y = (toField.getPosition().getY() + fromField.getPosition().getY()) / 2;
                Field field = board[x][y];
                field.resetPiece();
                Piece piece = fromField.getPiece();
                fromField.resetPiece();
                toField.setPiece(piece);
            } else {
                fromField.unselectPiece();
            }
        }
    }

    private boolean isProperDirection(Field fromField, Field toField) {
        int yFrom = fromField.getPosition().getY();
        int yTo = toField.getPosition().getY();
        switch (gameModel.getActivePlayer()) {
            case PLAYER_1:
                return yTo > yFrom;
            case PLAYER_2:
                return yTo < yFrom;
        }
        return false;
    }

    private boolean canBeatEnemy(Field fromField, Field toField) {
        boolean diagonalTwoFieldsNeighbourhoodCondition =
                fromField.getPosition().isTwoFieldsDiagonalNeighbourhood(toField.getPosition());
        boolean noPieceOnToFieldCondition = !toField.containsPiece();
        if (diagonalTwoFieldsNeighbourhoodCondition && noPieceOnToFieldCondition) {
            Player activePlayer = gameModel.getActivePlayer();
            int x = (toField.getPosition().getX() + fromField.getPosition().getX()) / 2;
            int y = (toField.getPosition().getY() + fromField.getPosition().getY()) / 2;
            Field field = board[x][y];
            if (field.containsPiece() && !field.getPiece().getPlayer().equals(activePlayer)) {
                return true;
            }
        }
        return false;
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

    private void resetSelection() {
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece()) {
                    field.unselectPiece();
                }
            }
        }
    }

    public void backupBoard() {
        boardBackup = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                boardBackup[i][j] = board[i][j].getCopy();
            }
        }
    }

    public void retreiveBackup() {
        board = boardBackup;
        setChanged();
        notifyObservers();
    }

    public Field[][] getBoard() {
        return board;
    }

}
