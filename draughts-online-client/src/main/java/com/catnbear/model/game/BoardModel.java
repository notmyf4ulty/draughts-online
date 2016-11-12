package com.catnbear.model.game;

import java.util.*;

public class BoardModel extends Observable {
    private static final int BOARD_DIMENSION = 8;
    private final Map<Position,Field> board;
    private GameModel gameModel;

    public BoardModel() {
        board = generateBoard();
        gameModel = GameModel.getInstance();
    }

    private Map<Position,Field> generateBoard() {
        Map<Position,Field> board = new LinkedHashMap<>();
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                Position position = new Position(i,j);
                Field field = setInitialConfiguration(position);
                board.put(position,field);
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
        field.assignToBoard(this);
        return field;
    }

    public void clickField(Position position) {
        Field clickedField = board.get(position);
        Player activePlayer = gameModel.getActivePlayer();
        if (isActivePiece() && !clickedField.containsPiece()) {
            moveActivePiece(position);
            resetSelection();
        } else {
            if (clickedField.containsPiece() &&
                    clickedField.getPiece().getPlayer().equals(activePlayer)) {
                resetSelection();
                clickedField.selectPiece();
            }
        }
        setChanged();
        notifyObservers();
    }


    private void moveActivePiece(Position position) {
        Field startField = getFieldOfActivePiece();
        Piece piece = startField.getPiece();
        startField.unselectPiece();
        startField.resetPiece();
        Field stopField = board.get(position);
//        piece.setPosition(position);
        stopField.setPiece(piece);
        stopField.selectPiece();
    }

    private Field getFieldOfActivePiece() {
        Set<Map.Entry<Position, Field>> entries = board.entrySet();
        for (Map.Entry<Position, Field> positionFieldEntry : entries) {
            Field field = positionFieldEntry.getValue();
            if (field.containsPiece() && field.isPieceSelcted()) {
                return field;
            }
        }
        return null;
    }

    private boolean isActivePiece() {
        return getFieldOfActivePiece() != null;
    }

    private void resetSelection() {
        for (Map.Entry<Position, Field> positionFieldEntry : board.entrySet()) {
            Field field = positionFieldEntry.getValue();
            if (field.containsPiece()) {
                field.unselectPiece();
            }
        }
    }

    public Map<Position, Field> getFields() {
        return board;
    }

    public Field getField(Position position) {
        return board.get(position);
    }
}
