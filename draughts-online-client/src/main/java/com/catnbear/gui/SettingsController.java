package com.catnbear.gui;

import com.catnbear.communication.ConnectionParameters;
import com.catnbear.utilities.GuiModifier;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

/**
 * Created by przemek on 17.11.16.
 */
public class SettingsController {

    @FXML
    StackPane mainPane;

    @FXML
    private TextField hostNameTextField;

    @FXML
    private TextField portNumberTextField;

    private ConnectionParameters connectionParameters;

    @FXML
    private void initialize() {
        connectionParameters = ConnectionParameters.getInstance();
    }

    @FXML
    private void defaultHostNameButtonCallback() {
        hostNameTextField.setText(ConnectionParameters.getDefaultHostName());
    }

    @FXML
    private void defaultPortNumberButtonCallback() {
        portNumberTextField.setText(Integer.toString(ConnectionParameters.getDefaultPortNumber()));
    }

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

    private void cleanTextFields() {
        connectionParameters.setDefaultParameters();
        hostNameTextField.setText(connectionParameters.getHostName());
        portNumberTextField.setText(Integer.toString(connectionParameters.getPortNumber()));
    }

    @FXML
    private void exitButtonCallback() {
        GuiModifier.changeWindow(mainPane, "/gui/welcomescreen.fxml", this);
    }
}
