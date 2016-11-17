package com.catnbear.gui;

import com.catnbear.utilities.GuiModifier;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class WelcomeScreenController {

    /**
     * Window's main pane.
     */
    @FXML
    private StackPane mainPane;

    /**
     * Callback of playButton's action.
     */
    @FXML
    private void playButtonCallback() {
        GuiModifier.changeWindow(mainPane, "/gui/boardwindow.fxml", this);
    }

    /**
     * Callback of settingsButton's action.
     */
    @FXML
    private void settingsButtonCallback() {
        GuiModifier.changeWindow(mainPane, "/gui/settings.fxml", this);
    }

    /**
     * Callback of exitButton's action.
     */
    @FXML
    private void exitButtonCallback() {
        Platform.exit();
    }
}

