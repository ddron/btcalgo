package com.btcalgo.ui;

import com.btcalgo.service.LicenseService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LicenseController {

    private Stage popup;
    private VBox popupVBox;

    private LicenseService licenseService;

    private String paymentUrl;

    public void initLicensePopup(MainPageController main) {
        popup = new Stage();
        popup.initOwner(main.getView().getScene().getWindow());
        popup.initModality(Modality.WINDOW_MODAL);
        popup.setResizable(false);
        popup.setTitle("BtcAlgo License");

        popupVBox = new VBox();
        popupVBox.setPadding(new Insets(20, 25, 10, 25));
        popupVBox.setSpacing(10);


        Scene popupScene = new Scene(popupVBox);
        popupScene.getStylesheets().add(
                MainPageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        popup.setScene(popupScene);
    }

    public void showLicensePopup() {
        if (licenseService.hasValidLicense()) {
            showActivatedLicensePage();
        } else {
            showTrialLicensePage();
        }
    }

    private void showTrialLicensePage() {
        popupVBox.getChildren().clear();

        // License type: TRIAL
        HBox hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.CENTER);
        hBox_1.getChildren().add(new Label("License type: "));
        Label trial = new Label("TRIAL");
        trial.getStyleClass().add("trial");
        hBox_1.getChildren().add(trial);
        popupVBox.getChildren().add(hBox_1);

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.BASELINE_LEFT);
        vBox1.getChildren().add(new Label("Trial license has following restrictions:"));
        vBox1.getChildren().add(new Label("1. Only 2 live orders could be run simultaneously"));
        vBox1.getChildren().add(new Label("2. Application runtime is 30 minutes. Then program will exit."));
        vBox1.getChildren().add(new Label("   You will need to re-run it and enter your orders again"));
        popupVBox.getChildren().add(vBox1);

        VBox vBox2 = new VBox();
        vBox2.getChildren().add(new Label("To get Full BtcAlgo version enter license key below:"));
        vBox2.getChildren().add(new Label("(to get key visit " + paymentUrl + ")"));
        vBox2.setPadding(new Insets(20, 0, 10, 0));
        vBox2.setAlignment(Pos.CENTER);
        popupVBox.getChildren().add(vBox2);

        final TextField licenseKey = new TextField();
        licenseKey.setMaxWidth(300);
        licenseKey.setPromptText("Enter Licence Key");
        popupVBox.getChildren().add(licenseKey);
        licenseKey.getParent().requestFocus();

        Button activate = new Button("Activate");
        activate.setPrefHeight(40);
        activate.setPrefWidth(120);
        activate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                licenseService.validate(licenseKey.getText());
            }
        });

        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(10, 0, 10, 0));
        vBox3.setAlignment(Pos.CENTER);
        vBox3.getChildren().add(activate);

        popupVBox.getChildren().add(vBox3);

        popup.show();
    }

    private void showActivatedLicensePage() {
        popupVBox.getChildren().clear();

        HBox hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.CENTER);
        hBox_1.setPadding(new Insets(10, 0, 10, 0));
        hBox_1.getChildren().add(new Label("License type: "));

        Label trial = new Label("FULL");
        trial.getStyleClass().add("full");
        hBox_1.getChildren().add(trial);

        popupVBox.getChildren().add(hBox_1);
        popupVBox.getChildren().add(new Label("You have fully functional version of BtcAlgo"));
        popupVBox.getChildren().add(new Label("License key: " + licenseService.getLicenseKey()));

        popup.show();
    }

    /*public void handleValidateLicense(ActionEvent actionEvent) {
        licenseStatus.setText("");
        String licenseKeyString = licenseKey.getText();
        boolean result = licenseService.validate(licenseKeyString);

        if (result) {
            licenseStatus.setText("VALID!!! Congrats Andrey!");
        } else {
            licenseStatus.setText("Not valid :-)");
        }
    }*/

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
