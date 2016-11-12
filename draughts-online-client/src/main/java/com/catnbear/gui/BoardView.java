package com.catnbear.gui;

import com.catnbear.model.game.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class BoardView extends GridPane implements Observer {
    private BoardModel boardModel;
    private GameModel gameModel;

    public BoardView(BoardModel boardModel) {
        super();
        this.gameModel = GameModel.getInstance();
        this.boardModel = boardModel;
        boardModel.addObserver(this);
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
        clearPieces();
        Iterator iterator = boardModel.getFields().entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry pair = (Map.Entry) iterator.next();
            Field field = (Field) pair.getValue();
            if (field.containsPiece()) {
                putPieceView(field.getPosition(), field.getPiece());
            }
        }
        iterator.remove();
    }

    private void clearPieces() {
        for(Node node : getChildren()) {
            if (Pane.class.isAssignableFrom(node.getClass())) {
                ((Pane)node).getChildren().clear();
            }
        }
    }

    private FieldView createFieldView(Position position) {
        int FIELD_SIDE_LENGTH = 50;
        FieldView fieldView = new FieldView();
        fieldView.setPrefWidth(FIELD_SIDE_LENGTH);
        fieldView.setPrefHeight(FIELD_SIDE_LENGTH);
        fieldView.setColor(position);
        fieldView.setOnMouseClicked(mouseEvent -> boardModel.clickField(position));
        return fieldView;
    }

    private void putPieceView(Position position, Piece piece) {
        FieldView fieldView = getFieldView(position);
        PieceView pieceView = new PieceView(piece);
        if (fieldView != null) {
            fieldView.putPieceView(pieceView);
        }
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

    @Override
    public void update(Observable observable, Object o) {
        refresh();
    }
}
