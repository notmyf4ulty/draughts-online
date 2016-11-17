package com.catnbear.gui;

import com.catnbear.communication.ConnectionParameters;
import com.catnbear.utilities.GuiModifier;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Controller of the Settings window.
 */
public class SettingsController {

    /**
     * Window's main pane.
     */
    @FXML
    StackPane mainPane;

    /**
     * TextField for putting a host's name.
     */
    @FXML
    private TextField hostNameTextField;

    /**
     * TextField for putting a port's number.
     */
    @FXML
    private TextField portNumberTextField;

    /**
     * ConnectionParameters class' instance.
     */
    private ConnectionParameters connectionParameters;

    /**
     * Window's initialization method.
     */
    @FXML
    private void initialize() {
        connectionParameters = ConnectionParameters.getInstance();
    }

    /**
     * Callback of defaultHostNameButton's action.
     */
    @FXML
    private void defaultHostNameButtonCallback() {
        hostNameTextField.setText(ConnectionParameters.getDefaultHostName());
    }

    /**
     * Callback of defaultPortNumberButton's action.
     */
    @FXML
    private void defaultPortNumberButtonCallback() {
        portNumberTextField.setText(Integer.toString(ConnectionParameters.getDefaultPortNumber()));
    }

    /**
     * Callback of saveButton's action.
     */
    @FXML
    private void saveButtonCallback() {
        if (!(hostNameTextField.getText().equals("") || portNumberTextField.equals(""))) {
            try {
                connectionParameters.setHostName(hostNameTextField.getText());
                connectionParameters.setPortNumber(Integer.parseInt(portNumberTextField.getText()));
            } catch (NumberFormatException e) {
                cleanTextFields();
            }
        } else {
            cleanTextFields();
        }
    }

    /**
     * Cleans text fields.
     */
    private void cleanTextFields() {
        connectionParameters.setDefaultParameters();
        hostNameTextField.setText(connectionParameters.getHostName());
        portNumberTextField.setText(Integer.toString(connectionParameters.getPortNumber()));
    }

    /**
     * Callback of exitButtons action.
     */
    @FXML
    private void exitButtonCallback() {
        GuiModifier.changeWindow(mainPane, "/gui/welcomescreen.fxml", this);
    }
}
