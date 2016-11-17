package com.catnbear.gui;

import com.catnbear.communication.ConnectionParams;
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

    private ConnectionParams connectionParams;

    @FXML
    private void initialize() {
        connectionParams = ConnectionParams.getInstance();
    }

    @FXML
    private void defaultHostNameButtonCallback() {
        hostNameTextField.setText(ConnectionParams.getDefaultHostName());
    }

    @FXML
    private void defaultPortNumberButtonCallback() {
        portNumberTextField.setText(Integer.toString(ConnectionParams.getDefaultPortNumber()));
    }

    @FXML
    private void saveButtonCallback() {
        if (!(hostNameTextField.getText().equals("") || portNumberTextField.equals(""))) {
            try {
                connectionParams.setHostName(hostNameTextField.getText());
                connectionParams.setPortNumber(Integer.parseInt(portNumberTextField.getText()));
            } catch (NumberFormatException e) {
                cleanTextFields();
            }
        } else {
            cleanTextFields();
        }
    }

    private void cleanTextFields() {
        connectionParams.setDefaultParameters();
        hostNameTextField.setText(connectionParams.getHostName());
        portNumberTextField.setText(Integer.toString(connectionParams.getPortNumber()));
    }

    @FXML
    private void exitButtonCallback() {
        GuiModifier.changeWindow(mainPane, "/gui/welcomescreen.fxml", this);
    }
}
