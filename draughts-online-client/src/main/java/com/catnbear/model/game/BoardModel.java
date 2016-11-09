package com.catnbear.model.game;

public class BoardModel {
    private static final int BOARD_DIMENSION = 8;

    private static BoardModel instance;
    private final Field [][] board;

    private BoardModel() {
        board = createBoard();
    }

    public static BoardModel getInstance() {
        if (instance == null) {
            instance = new BoardModel();
        }
        return instance;
    }

    private Field[][] createBoard() {
        Field [][] board = new Field[BOARD_DIMENSION][BOARD_DIMENSION];
        for (int i = 0 ; i < board.length ; i++) {
            for (int j = 0 ; j < board[i].length ; j++) {
                Position position = new Position(i,j);
                if (isFieldWhite(position)) {
                    board[i][j] = new Field(Field.FieldColor.COLOR_1, position);
                } else {
                    board[i][j] = new Field(Field.FieldColor.COLOR_2, position);
                }
            }
        }
        return board;
    }

    private boolean isFieldWhite(Position position) {
        return (((position.getX() + position.getY()) % 2) == 0);
    }

    public Field[][] getBoard() {
        return board;
    }
}
