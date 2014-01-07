package com.btcalgo.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SinglePageController {

    @FXML private Text status;
    @FXML private GridPane view;
    @FXML private Button validate;
    @FXML private TextField key;
    @FXML private PasswordField secret;

    public void handleValidateOnClick(ActionEvent actionEvent) {
        validate.setText("Validating...");
        status.setText("Sign in button pressed");
    }

    public GridPane getView() {
        return view;
    }
}
