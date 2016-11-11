package com.catnbear.gui;

import com.catnbear.model.game.BoardModel;
import com.catnbear.model.game.Field;
import com.catnbear.model.game.Position;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.Iterator;
import java.util.Map;

public class BoardView extends GridPane {
    private BoardModel boardModel;

    public BoardView(BoardModel boardModel) {
        super();
        this.boardModel = boardModel;
        createBoard();
        refresh();
    }

    private void createBoard() {
        Iterator iterator = boardModel.getFields().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Position position = (Position) pair.getKey();
            FieldView fieldView = createFieldView(position);
            add(fieldView, position.getX(), position.getY());
        }
        iterator.remove();
    }

    private void refresh() {
        Iterator iterator = boardModel.getFields().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Field field = (Field) pair.getValue();
            if (field.containsPiece()) {
                putPieceView(new Position(field.getPosition().getX(), field.getPosition().getY()),
                        new PieceView(field.getPiece()));
            }
        }
    }

    public FieldView createFieldView(Position position) {
        int FIELD_SIDE_LENGTH = 50;
        FieldView fieldView = new FieldView();
        fieldView.setPrefWidth(FIELD_SIDE_LENGTH);
        fieldView.setPrefHeight(FIELD_SIDE_LENGTH);
        fieldView.setColor(position);
        return fieldView;
    }

    public void putPieceView(Position position, PieceView pieceView) {
        FieldView fieldView = getFieldView(position);
        if (fieldView != null) {
            fieldView.putPieceView(pieceView);
        }
    }

    public void deletePiece(Position position) {

    }

    public boolean hasPiece(Position position) {
        return true;
    }

    private FieldView getFieldView(Position position) {
        int columnIndex = position.getX();
        int rowIndex = position.getY();
        for (Node node : getChildren()) {
            if(getRowIndex(node) == rowIndex &&
                    getColumnIndex(node) == columnIndex &&
                    node.getClass().equals(FieldView.class)) {
                return (FieldView) node;
            }
        }
        return null;
    }
}
