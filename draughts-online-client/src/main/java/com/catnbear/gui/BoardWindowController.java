package com.catnbear.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.*;

public class BoardWindowController {

    @FXML
    VBox mainPane;

    @FXML
    private void initialize() {
        generateBoard();
    }

    public void generateBoard() {
        final int BOARD_DIMENSION = 8;
        final int FIELD_SIDE_LENGTH = 50;
        final String EVEN_FIELD_STYLE = "-fx-background-color: white;";
        final String ODD_FIELD_STYLE = "-fx-background-color: gray;";

        GridPane gridPane = new GridPane();
        for (int i = 0 ; i < BOARD_DIMENSION ; i++) {
            for (int j = 0 ; j < BOARD_DIMENSION ; j++) {
                StackPane stackPane = new StackPane();
                stackPane.setPrefWidth(FIELD_SIDE_LENGTH);
                stackPane.setPrefHeight(FIELD_SIDE_LENGTH);
                if ((i + j) % 2 == 0) {
                    stackPane.setStyle(EVEN_FIELD_STYLE);
                } else {
                    stackPane.setStyle(ODD_FIELD_STYLE);
                }
                gridPane.add(stackPane,i,j);
            }
        }
        mainPane.getChildren().add(gridPane);
    }
}
