package com.catnbear.gui;

import com.catnbear.model.game.Position;
import javafx.scene.layout.StackPane;

class FieldView extends StackPane {

    FieldView() {
        super();
    }

    void setColor(Position position) {
        if (position.isXySumEven()) {
            String field_color_1_style = "-fx-background-color: #c5925e;";
            setStyle(field_color_1_style);
        } else {
            String field_color_2_style = "-fx-background-color: #9f5000;";
            setStyle(field_color_2_style);
        }
    }

    void putPieceView(PieceView pieceView) {
        getChildren().clear();
        getChildren().add(pieceView);
    }
}
