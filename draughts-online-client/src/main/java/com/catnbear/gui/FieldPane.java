package com.catnbear.gui;

import com.catnbear.model.game.Position;
import javafx.scene.layout.StackPane;

/**
 * Pane for placing a pieces.
 */
class FieldPane extends StackPane {

    /**
     * Default constructor.
     */
    FieldPane() {
        super();
    }

    /**
     * Sets pane's color regarding to the position. Purpose of this function is to create an alternately colorized
     * game board.
     * @param position Field's position.
     */
    void setColor(Position position) {
        if (position.isXySumEven()) {
            String field_color_1_style = "-fx-background-color: #c5925e;";
            setStyle(field_color_1_style);
        } else {
            String field_color_2_style = "-fx-background-color: #9f5000;";
            setStyle(field_color_2_style);
        }
    }

    /**
     * Puts a piece's shape on a field.
     * @param pieceShape Piece to be placed.
     */
    void putPieceShape(PieceShape pieceShape) {
        getChildren().clear();
        getChildren().add(pieceShape);
    }
}
