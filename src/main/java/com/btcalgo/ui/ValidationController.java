package com.btcalgo.ui;

import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.Direction;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.util.StringUtils;
import com.google.common.base.Strings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

import static com.btcalgo.ui.ValidationErrors.ErrorType.*;
import static com.btcalgo.ui.ValidationErrors.Field.*;
import static com.btcalgo.ui.ValidationErrors.getErrorValue;

public class ValidationController {

    private Stage popup;
    private VBox errorsContent;

    private ApiService apiService;
    private String paymentUrl;

    public void initValidationPopup(MainPageController main) {
        popup = new Stage();
        popup.initOwner(main.getView().getScene().getWindow());
        popup.initModality(Modality.WINDOW_MODAL);
        //popup.setResizable(false);
        popup.setTitle("Error!");

        VBox popupVBox = new VBox();
        popupVBox.setPadding(new Insets(20, 25, 10, 25));
        popupVBox.setSpacing(10);
        Label titleLabel = new Label("Following errors should be fixed:");
        titleLabel.setPrefWidth(300);
        titleLabel.setAlignment(Pos.CENTER);
        popupVBox.getChildren().add(titleLabel);

        errorsContent = new VBox();
        errorsContent.setPadding(new Insets(10, 0, 15, 0));
        errorsContent.setSpacing(10);
        popupVBox.getChildren().add(errorsContent);

        Button okBtn = new Button("OK");
        okBtn.setPrefHeight(40);
        okBtn.setPrefWidth(120);
        okBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                popup.hide();
            }
        });
        popupVBox.getChildren().add(okBtn);

        Scene popupScene = new Scene(popupVBox);
        popupScene.getStylesheets().add(
                MainPageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        popup.setScene(popupScene);
    }

    public void showValidationPopup(List<String> errors) {
        errorsContent.getChildren().clear();
        for (String error : errors) {
            errorsContent.getChildren().add(new Label(error));
        }
        popup.show();
    }

    public void showUnableToPlacePopup() {
        errorsContent.getChildren().clear();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        errorsContent.getChildren().add(vBox);

        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox1);
        hBox1.getChildren().add(new Label("with "));
        Label trial_label = new Label("TRIAL");
        trial_label.getStyleClass().add("small_trial");
        hBox1.getChildren().add(trial_label);
        hBox1.getChildren().add(new Label(" license"));

        vBox.getChildren().add(new Label("only two orders can run simultaneously"));

        HBox hBox2 = new HBox();
        hBox2.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox2);
        hBox2.getChildren().add(new Label("to get "));
        Label full_label = new Label("FULL");
        full_label.getStyleClass().add("small_full");
        hBox2.getChildren().add(full_label);
        hBox2.getChildren().add(new Label(" license go to " + paymentUrl));

        popup.show();
    }

    public List<String> validateOrderFields(MainPageController main) {
        List<String> result = new ArrayList<>();
        if (!apiService.hasValidKeys()) {
            result.add("Enter and validate correct keys before submitting an order");
            return result;
        }

        // check strategy type
        if (StrategyType.valueByDisplayName(main.getStrategyTypes().getSelectionModel().getSelectedItem()) != StrategyType.STOP_LOSS) {
            result.add(ValidationErrors.getErrorValue(STRATEGY_TYPE, INCORRECT_VALUE));
        }

        // check side
        if (Direction.valueByDisplayName(main.getDirection().getSelectionModel().getSelectedItem()) == Direction.NONE) {
            result.add(ValidationErrors.getErrorValue(DIRECTION, INCORRECT_VALUE));
        }

        // check amount
        String amountValue = main.getAmount().getText();
        if (Strings.isNullOrEmpty(amountValue)) {
            result.add(ValidationErrors.getErrorValue(AMOUNT, EMPTY));
        } else if (!StringUtils.isNumber(amountValue)) {
            result.add(ValidationErrors.getErrorValue(AMOUNT, FORMAT));
        }

        // stop price
        String stopPriceValue = main.getStopPrice().getText();
        if (Strings.isNullOrEmpty(stopPriceValue)) {
            result.add(ValidationErrors.getErrorValue(STOP_PRICE, EMPTY));
        } else if (!StringUtils.isNumber(stopPriceValue)) {
            result.add(ValidationErrors.getErrorValue(STOP_PRICE, FORMAT));
        }

        // limit price
        String limitPriceValue = main.getLimitPrice().getText();
        if (Strings.isNullOrEmpty(limitPriceValue)) {
            result.add(ValidationErrors.getErrorValue(LIMIT_PRICE, EMPTY));
        } else if (!StringUtils.isNumber(limitPriceValue)) {
            result.add(ValidationErrors.getErrorValue(LIMIT_PRICE, FORMAT));
        }

        return result;
    }

    public List<String> validateKeys(MainPageController main) {
        List<String> result = new ArrayList<>();
        if (Strings.isNullOrEmpty(main.getKey().getText())) {
            result.add(getErrorValue(KEY, EMPTY));
        }
        if (Strings.isNullOrEmpty(main.getSecret().getText())) {
            result.add(getErrorValue(SECRET, EMPTY));
        }
        return result;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}
