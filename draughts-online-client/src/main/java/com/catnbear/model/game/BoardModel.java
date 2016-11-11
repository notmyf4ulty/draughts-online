package com.catnbear.model.game;

import java.util.Map;

public class BoardModel {
    private static final int BOARD_DIMENSION = 8;

    private final Field [][] board;

    public BoardModel() {
        board = createBoard();
    }

    private Field[][] createBoard() {
        Field [][] board = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[i].length ; j++) {
                Position position = new Position(i,j);
                board[i][j] = setInitialConfiguration(position);
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
        return field;
    }

    private boolean isFieldWhite(Position position) {
        return (((position.getX() + position.getY()) % 2) == 0);
    }

    public Field[][] getFields() {
        return board;
    }

    public Field getField(Position position) {
        return board[position.getX()][position.getY()];
    }
}
