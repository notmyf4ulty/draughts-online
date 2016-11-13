package com.catnbear.model.game;

import java.util.*;

public class Board extends Observable {
    private static final int BOARD_DIMENSION = 5;
    private Field [][] board;
    private Field [][] boardBackup;
    private GameModel gameModel;
    private boolean multiBeatMode;
    private Field multiBeatingPieceField;

    public Board() {
        board = generateBoard();
        backupBoard();
        gameModel = GameModel.getInstance();
        gameModel.assignBoardModel(this);
        multiBeatMode = false;
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

    public void chooseField(Position position) {
        Field chosenField = board[position.getX()][position.getY()];
        if (gameModel.isMoveAvailable()) {
            Field activePieceField;
            if (multiBeatMode) {
                activePieceField = multiBeatingPieceField;
            } else {
                activePieceField = getFieldOfActivePiece();
            }

            if (activePieceField != null) {
                moveActivePiece(activePieceField, chosenField);
            }
            if (chosenField.containsPiece()) {
                if (!multiBeatMode) {
                    selectPiece(chosenField);
                }
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
                    if (!multiBeatMode) {
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
        if (!multiBeatMode) {
            resetSelection();
        }
    }

    private boolean isProperDirection(Position fromPosition, Position toPosition) {
        if(Position.getMoveDistance(fromPosition,toPosition).equals(MoveDistance.ONE_FIELD_DISTANCE)) {
            int yFrom = fromPosition.getY();
            int yTo = toPosition.getY();
            switch (gameModel.getActivePlayer()) {
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
        if (enemyField.containsPiece() &&
                !enemyField.getPiece().getPlayer().equals(activePlayer)) {
            enemyField.resetPiece();
            movePiece(fromField,toField);
            multiBeatMode = isOpponentToBeatAround(toField);
            if (multiBeatMode) {
                gameModel.setMoveAvailable(true);
            }
        }
    }

    private boolean isOpponentToBeatAround(Field field) {
        Field [] opponentFields = getNeighbouringOpponents(field);
        if (isPlaceBehindOpponents(field, opponentFields)) {
            multiBeatingPieceField = field;
            return true;
        } else {
            multiBeatingPieceField = null;
        }
        return false;
    }

    private Field [] getNeighbouringOpponents(Field field) {
        ArrayList<Field> opponentFields = new ArrayList<>();
        Position [] neighbouringPositions = field.getPosition().getDiagonalNeighboursPositions(1,BOARD_DIMENSION);
        for (Position position : neighbouringPositions) {
            Field opponentField = board[position.getX()][position.getY()];
            if (opponentField.containsPiece() &&
                    !opponentField
                            .getPiece()
                            .getPlayer()
                            .equals(gameModel.getActivePlayer())) {
                opponentFields.add(opponentField);
            }
        }

        Field [] opponentFieldsArray = new Field[opponentFields.size()];
        opponentFieldsArray = opponentFields.toArray(opponentFieldsArray);
        return opponentFieldsArray;
    }

    private boolean isPlaceBehindOpponents(Field attackingField, Field [] opponents) {
        Position [] neighbouringPositions = attackingField.getPosition().getDiagonalNeighboursPositions(2,BOARD_DIMENSION);
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

    private boolean isOpponentInTheMiddle(Position fromPosition, Position toPosition) {
        int opponentX = (fromPosition.getX() + toPosition.getX()) / 2;
        int opponentY = (fromPosition.getY() + toPosition.getY()) / 2;
        if (opponentX != 0 && opponentY != 0) {
            Field opponentField = board[opponentX][opponentY];
            if (opponentField.containsPiece()) {
                return !opponentField.getPiece().getPlayer().equals(gameModel.getActivePlayer());
            }
        }
        return false;
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
        multiBeatMode = false;
        multiBeatingPieceField = null;
        setChanged();
        notifyObservers();
    }

    public Field[][] getBoard() {
        return board;
    }

}
