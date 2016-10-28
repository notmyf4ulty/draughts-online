package com.catnbear.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by przemek on 28.10.16.
 */
public class BoardModel {
    private static final int BOARD_DIMENSION = 8;

    private static BoardModel instance;
    private final List<List<Field>> board;

    private BoardModel() {
        board = fillBoard();
    }

    public static BoardModel getInstance() {
        if (instance == null) {
            instance = new BoardModel();
        }
        return instance;
    }

    private List<List<Field>> fillBoard() {
        List<List<Field>> board = new ArrayList<>();
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            ArrayList<Field> fields = new ArrayList<>();
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                Field field;
                if ((i + j) % 2 == 0) {
                    field = new Field(Field.FieldColor.COLOR_1, new Position(i,j));
                } else {
                    field = new Field(Field.FieldColor.COLOR_2, new Position(i,j));
                }
                fields.add(field);
            }
            board.add(fields);
        }
        return board;
    }

    public List<List<Field>> getBoard() {
        return board;
    }
}
