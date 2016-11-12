package com.catnbear.gui;

import com.catnbear.model.game.Position;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class FieldView extends StackPane {
    final String FIELD_COLOR_1_STYLE = "-fx-background-color: #c5925e;";
    final String FIELD_COLOR_2_STYLE = "-fx-background-color: #9f5000;";

    FieldView() {
        super();
    }

    void setColor(Position position) {
        if (position.isXySumEven()) {
            setStyle(FIELD_COLOR_1_STYLE);
        } else {
            setStyle(FIELD_COLOR_2_STYLE);
        }
    }

    void putPieceView(PieceView pieceView) {
        getChildren().clear();
        getChildren().add(pieceView);
    }

    public void clear() {
        getChildren().clear();
    }

    private boolean containsPieceView() {
        for (Node node : getChildren()) {
            if (node.getClass().equals(PieceView.class)) {
                return true;
            }
        }
        return false;
    }
}
