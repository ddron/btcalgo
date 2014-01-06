package com.btcalgo.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SinglePageController {

    public PasswordField passwordField;
    public Text actiontarget;
    public GridPane view;
    public Button loginButton;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        actiontarget.setText("Sign in button pressed");
    }
}
