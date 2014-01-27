package com.btcalgo.ui;

import com.btcalgo.service.LicenseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    // licensing
    @FXML private Label licenseStatus;
    @FXML private TextField licenseKey;
    @FXML private Button validateLicense;

    private LicenseService licenseService;

    public void initLicensePopup(MainPageController main) {
        popup = new Stage();
        popup.initOwner(main.getView().getScene().getWindow());
        popup.initModality(Modality.WINDOW_MODAL);
        //popup.setResizable(false);
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

        HBox hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.BASELINE_LEFT);
        hBox_1.getChildren().add(new Label("License type: "));

        Label trial = new Label("TRIAL");
        trial.getStyleClass().add("trial");
        hBox_1.getChildren().add(trial);

        popupVBox.getChildren().add(hBox_1);

        popup.show();
    }

    private void showActivatedLicensePage() {
        popupVBox.getChildren().clear();

        HBox hBox_1 = new HBox();
        hBox_1.setAlignment(Pos.BASELINE_LEFT);
        hBox_1.getChildren().add(new Label("License type: "));

        Label trial = new Label("FULL");
        trial.getStyleClass().add("full");
        hBox_1.getChildren().add(trial);

        popupVBox.getChildren().add(hBox_1);

        popup.show();
    }

    public void handleValidateLicense(ActionEvent actionEvent) {
        licenseStatus.setText("");
        String licenseKeyString = licenseKey.getText();
        boolean result = licenseService.validate(licenseKeyString);

        if (result) {
            licenseStatus.setText("VALID!!! Congrats Andrey!");
        } else {
            licenseStatus.setText("Not valid :-)");
        }
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }
}
