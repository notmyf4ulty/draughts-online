package com.catnbear.model.game;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;

public class BoardModel extends Observable {
    private static final int BOARD_DIMENSION = 8;
    private final Map<Position,Field> board;
    private GameModel gameModel;

    public BoardModel() {
        board = createBoard();
        gameModel = GameModel.getInstance();
    }

    private Map<Position,Field> createBoard() {
        Map<Position,Field> board = new HashMap<>();
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
        Field field;
        if (position.isXySumEven()) {
            field = new Field(Field.FieldColor.COLOR_1,position);
            if (position.getY() < 3) {
                field.setPiece(new Piece(Piece.PieceType.MEN,
                        Player.PLAYER_1,
                        position));
            } else if (position.getY() > 4) {
                field.setPiece(new Piece(Piece.PieceType.MEN,
                        Player.PLAYER_2,
                        position));
            }
        } else {
            field = new Field(Field.FieldColor.COLOR_2,position);
        }
        field.assignToBoard(this);
        return field;
    }

    public void clickField(Position position) {
        resetSelection();
        Field clickedField = board.get(position);
        Player activePlayer = gameModel.getActivePlayer();
        if (clickedField.containsPiece() &&
                clickedField.getPiece().getPlayer().equals(activePlayer)) {
            clickedField.selectPiece();
        }
        setChanged();
        notifyObservers();
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
