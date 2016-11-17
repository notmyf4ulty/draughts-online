package com.catnbear.gui;

import com.catnbear.model.game.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.Observable;
import java.util.Observer;

/**
 * Board's pane. Reflection of the board's logical model. Contains methods generating draughts board and fills it
 * according to the pieces' placement. It implements Observer interface to track changes in the Logical Board's model.
 */
class BoardPane extends GridPane implements Observer {
    /**
     * Logical model of game's board.
     */
    private Board board;

    /**
     * Board's based constructor. Fully initializes the GUI of the board.
     * @param board Board's instance.
     */
    BoardPane(Board board) {
        super();
        this.board = board;
        board.addObserver(this);
        createBoard();
        refresh();
    }

    /**
     * Creates GUI board.
     */
    private void createBoard() {
        Field [][] board = this.board.getBoard();
        for(Field [] fields : board) {
            for (Field field : fields) {
                Position position = field.getPosition();
                FieldPane fieldPane = createFieldPane(position);
                add(fieldPane, position.getX(), position.getY());
            }
        }
    }

    /**
     * Refreshes GUI board.
     */
    private void refresh() {
        clearPieces();
        Field [][] board = this.board.getBoard();
        for(Field [] fields : board) {
            for (Field field : fields) {
                if (field.containsPiece()) {
                    putPieceShape(field.getPosition(), field.getPiece());
                }
            }
        }
    }

    /**
     * Clears board's pieces.
     */
    private void clearPieces() {
        for(Node node : getChildren()) {
            if (Pane.class.isAssignableFrom(node.getClass())) {
                ((Pane)node).getChildren().clear();
            }
        }
    }

    /**
     * Creates FieldPane with a proper position.
     * @param position FieldPane's position.
     * @return Created FieldPane.
     */
    private FieldPane createFieldPane(Position position) {
        int FIELD_SIDE_LENGTH = 50;
        FieldPane fieldPane = new FieldPane();
        fieldPane.setPrefWidth(FIELD_SIDE_LENGTH);
        fieldPane.setPrefHeight(FIELD_SIDE_LENGTH);
        fieldPane.setColor(position);
        fieldPane.setOnMouseClicked(mouseEvent -> board.chooseField(position));
        return fieldPane;
    }

    /**
     * Puts PieceShape on a proper position.
     * @param position Position of a new PieceShape.
     * @param piece Piece's logical model.
     */
    private void putPieceShape(Position position, Piece piece) {
        FieldPane fieldPane = getFieldPane(position);
        PieceShape pieceShape = new PieceShape(piece);
        if (fieldPane != null) {
            fieldPane.putPieceShape(pieceShape);
        }
    }

    /**
     * Getter of the FieldPane from a given position.
     * @param position FieldPane's position.
     * @return FieldPane from a given position.
     */
    private FieldPane getFieldPane(Position position) {
        int columnIndex = position.getX();
        int rowIndex = position.getY();
        for (Node node : getChildren()) {
            if(getRowIndex(node) == rowIndex &&
                    getColumnIndex(node) == columnIndex &&
                    node.getClass().equals(FieldPane.class)) {
                return (FieldPane) node;
            }
        }
        return null;
    }

    @Override
    public void update(Observable observable, Object o) {
        refresh();
    }
}
