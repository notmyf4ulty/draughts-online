package com.catnbear.model.game;

import java.util.HashMap;
import java.util.Map;

public class BoardModel {
    private static final int BOARD_DIMENSION = 8;

    private final Map<Position,Field> board;

    public BoardModel() {
        board = createBoard();
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
                        Piece.PieceOwner.PLAYER_1,
                        position));
            } else if (position.getY() > 4) {
                field.setPiece(new Piece(Piece.PieceType.MEN,
                        Piece.PieceOwner.PLAYER_2,
                        position));
            }
        } else {
            field = new Field(Field.FieldColor.COLOR_2,position);
        }
        field.assignToBoard(this);
        return field;
    }

    public void selectField() {

    }

    public Map<Position, Field> getFields() {
        return board;
    }

    public Field getField(Position position) {
        return board.get(position);
    }
}
