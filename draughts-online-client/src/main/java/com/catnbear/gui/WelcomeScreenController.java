package com.catnbear.gui;

import com.catnbear.utilities.GuiModifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class WelcomeScreenController {
    @FXML
    private StackPane menuPane;

    @FXML
    private void playButtonCallback() {
        GuiModifier.changeWindow(menuPane, "/gui/boardwindow.fxml", this);
    }

    @FXML
    private void setttingsButtonCallback() {

    }

    @FXML
    private void exitButtonCallback() {
        Platform.exit();
    }
}

