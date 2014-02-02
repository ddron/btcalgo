package com.btcalgo.ui;

import com.btcalgo.service.LicenseService;
import com.google.common.base.Strings;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.stage.WindowEvent;

public class LicenseController {

    private Stage licensePopup;
    private VBox popupVBox;

    private LicenseService licenseService;

    private String paymentUrl;
    private String supportEmail;

    public void initLicensePopup(MainPageController main) {
        licensePopup = new Stage();
        licensePopup.initOwner(main.getView().getScene().getWindow());
        licensePopup.initModality(Modality.WINDOW_MODAL);
        licensePopup.setResizable(false);
        licensePopup.setTitle("BtcAlgo License");

        popupVBox = new VBox();
        popupVBox.setPadding(new Insets(20, 25, 10, 25));
        popupVBox.setSpacing(10);


        Scene popupScene = new Scene(popupVBox);
        popupScene.getStylesheets().add(
                MainPageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        licensePopup.setScene(popupScene);
    }

    public void showLicensePopup() {
        if (licenseService.hasValidLicense()) {
            showActivatedLicensePage();
        } else {
            showTrialLicensePage();
        }
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

        licensePopup.show();
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

        final Button activate = new Button("Activate");
        activate.setPrefHeight(40);
        activate.setPrefWidth(120);
        activate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                activate.setDisable(true);
                activate.setText("Activating...");
                if (isLicenseKeyTextValid(licenseKey.getText())) {
                    new Thread(new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            final boolean validLicense = licenseService.activateLicense(licenseKey.getText());
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (validLicense) {
                                        showActivationCompletedPopup();
                                    } else {
                                        showIncorrectKeyPopup();
                                    }
                                    activate.setText("Activate");
                                    activate.setDisable(false);
                                }
                            });
                            return validLicense;
                        }
                    }).start();
                } else {
                    showValidationPopup(licenseKey.getText());
                    activate.setText("Activate");
                    activate.setDisable(false);
                }
            }
        });

        VBox vBox3 = new VBox();
        vBox3.setPadding(new Insets(10, 0, 10, 0));
        vBox3.setAlignment(Pos.CENTER);
        vBox3.getChildren().add(activate);

        popupVBox.getChildren().add(vBox3);

        licensePopup.show();
    }

    private boolean isLicenseKeyTextValid(String key) {
        return !Strings.isNullOrEmpty(key) && isLicenseKeyFormatValid(key);
    }

    private boolean isLicenseKeyFormatValid(String key) {
        String[] parts = key.split("-");
        if (parts.length != 5) {
            return false;
        }
        for (String part : parts) {
            if (part.length() != 5) {
                return false;
            }
        }
        return true;
    }

    private void showValidationPopup(String key) {
        final Stage validationPopup = buildLicensePopupChild("Error!");
        VBox popupVBox = (VBox) validationPopup.getScene().getRoot();

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 0, 20, 0));
        vBox1.setAlignment(Pos.CENTER);
        if (Strings.isNullOrEmpty(key)) {
            vBox1.getChildren().add(new Label("License key is empty"));
        } else {
            vBox1.getChildren().add(new Label("License key format is incorrect"));
            vBox1.getChildren().add(new Label("Correct format is"));
            vBox1.getChildren().add(new Label("XXXXX-XXXXX-XXXXX-XXXXX-XXXXX"));
        }

        popupVBox.getChildren().add(vBox1);

        final Button ok = new Button("OK");
        ok.setMinHeight(40);
        ok.setMinWidth(120);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                validationPopup.hide();
            }
        });
        popupVBox.getChildren().add(ok);

        validationPopup.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                licensePopup.hide();
            }
        });

        validationPopup.show();
    }

    private void showIncorrectKeyPopup() {
        final Stage incorrectKeyPopup = buildLicensePopupChild("Error!");
        VBox popupVBox = (VBox) incorrectKeyPopup.getScene().getRoot();

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 0, 20, 0));
        vBox1.setAlignment(Pos.CENTER);
        vBox1.getChildren().add(new Label("License key is incorrect!"));
        vBox1.getChildren().add(new Label("Please note that the license key can be used only with one PC"));
        vBox1.getChildren().add(new Label("In case of questions please contact " + supportEmail));

        popupVBox.getChildren().add(vBox1);

        final Button ok = new Button("OK");
        ok.setMinHeight(40);
        ok.setMinWidth(120);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                incorrectKeyPopup.hide();
            }
        });
        popupVBox.getChildren().add(ok);

        incorrectKeyPopup.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                licensePopup.hide();
            }
        });

        incorrectKeyPopup.show();
    }

    private void showActivationCompletedPopup() {
        final Stage completePopup = buildLicensePopupChild("Success!");
        VBox popupVBox = (VBox) completePopup.getScene().getRoot();

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 0, 20, 0));
        vBox1.setAlignment(Pos.CENTER);
        vBox1.getChildren().add(new Label("Activation completed!"));
        popupVBox.getChildren().add(vBox1);

        final Button ok = new Button("OK");
        ok.setMinHeight(40);
        ok.setMinWidth(120);
        ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                completePopup.hide();
                licensePopup.hide();
            }
        });
        popupVBox.getChildren().add(ok);

        completePopup.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                licensePopup.hide();
            }
        });

        completePopup.show();
    }

    private Stage buildLicensePopupChild(String title) {
        final Stage licensePopupChild = new Stage();
        licensePopupChild.initOwner(licensePopup);
        licensePopupChild.initModality(Modality.WINDOW_MODAL);
        licensePopupChild.setResizable(false);
        licensePopupChild.setTitle(title);

        VBox popupVBox = new VBox();
        popupVBox.setAlignment(Pos.CENTER);
        popupVBox.setPadding(new Insets(20, 25, 10, 25));
        popupVBox.setSpacing(10);

        Scene popupScene = new Scene(popupVBox);
        popupScene.getStylesheets().add(
                MainPageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        licensePopupChild.setScene(popupScene);

        return licensePopupChild;
    }

    public void setLicenseService(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public void setSupportEmail(String supportEmail) {
        this.supportEmail = supportEmail;
    }
}
