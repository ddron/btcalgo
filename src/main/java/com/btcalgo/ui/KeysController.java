package com.btcalgo.ui;

import com.btcalgo.service.api.templates.InfoTemplate;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class KeysController {

    private Stage keysPopup;
    private VBox popupVBox;

    private volatile String message;
    private volatile boolean keysValid;

    private volatile boolean validatingNow;

    public void initKeysPopup(MainPageController main) {
        keysPopup = new Stage();
        keysPopup.initOwner(main.getView().getScene().getWindow());
        keysPopup.initModality(Modality.WINDOW_MODAL);
        keysPopup.setResizable(false);
        keysPopup.setTitle("BtcAlgo License");

        popupVBox = new VBox();
        popupVBox.setPadding(new Insets(20, 25, 10, 25));
        popupVBox.setSpacing(10);


        Scene popupScene = new Scene(popupVBox);
        popupScene.getStylesheets().add(
                MainPageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        keysPopup.setScene(popupScene);
    }

    public void showKeysPopup() {
        keysPopup.setTitle(keysValid ? "Success!" : "Error!");
        popupVBox.getChildren().clear();

        popupVBox.getChildren().add(new Label(message));

        final Button ok = new Button("OK");
        ok.setMinHeight(40);
        ok.setMinWidth(120);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                keysPopup.hide();
            }
        });
        popupVBox.getChildren().add(ok);

        keysPopup.show();
    }

    public void updateStatus(InfoTemplate infoTemplate) {
        if (infoTemplate.hasAllRights()) {
            message = "Key is valid";
            keysValid = true;
            validatingNow = false;
            return;
        }

        if (!infoTemplate.isSuccess()) {
            message = "Invalid key or secret (or no 'info' right)!";
        } else if (!infoTemplate.hasInfoRights() && !infoTemplate.hasTradeRights()) {
            message = "Key does not have 'info' and 'trade' rights!";
        } else if (!infoTemplate.hasInfoRights()) {
            message = "Key does not have 'info' right!";
        } else if (!infoTemplate.hasTradeRights()) {
            message = "Key does not have 'trade' right!";
        }
        keysValid = false;
        validatingNow = false;
    }

    public boolean isValidatingNow() {
        return validatingNow;
    }

    public void setValidatingNow(boolean validatingNow) {
        this.validatingNow = validatingNow;
    }

}
