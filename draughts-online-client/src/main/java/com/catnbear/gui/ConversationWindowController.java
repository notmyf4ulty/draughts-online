package com.catnbear.gui;

import com.catnbear.model.CommunicationModel;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.PrintWriter;

public class ConversationWindowController {

    @FXML
    TextField userInputTextField;

    @FXML
    ListView<String> conversationListView;

    CommunicationModel communicationModel;

    @FXML
    private void initialize() {
        System.out.println("initializing");

        communicationModel = CommunicationModel.getInstance();
        communicationModel.stringPropertyProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.equals("")) {
                Platform.runLater(() -> conversationListView.getItems().add(newValue));
            }
        });
    }

    @FXML
    private void sendMessageButtonClicked() {
        String message = userInputTextField.getText();
        PrintWriter out;
        try {
            out = new PrintWriter(communicationModel.getEchoSocket().getOutputStream(), true);
            out.println(message);
//            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userInputTextField.selectAll();
    }
}
