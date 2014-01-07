package com.btcalgo.ui;

import com.btcalgo.CommandEnum;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.ui.model.KeysStatusHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import reactor.core.Reactor;

public class SinglePageController {

    @FXML private Text keysStatus;
    @FXML private GridPane view;
    @FXML private Button validate;
    @FXML private TextField key;
    @FXML private PasswordField secret;

    private Reactor reactor;
    private ApiService apiService;
    private KeysStatusHolder keysStatusHolder;

    public void initBindings() {
        keysStatus.textProperty().bind(keysStatusHolder.keysStatusProperty());
        validate.disableProperty().bind(keysStatusHolder.validateBtnDisabledProperty());
    }

    public void handleValidateOnClick(ActionEvent actionEvent) {
        keysStatusHolder.setValidateBtnDisabled(true);
        keysStatusHolder.setValidatingStatus();

        apiService.updateKeys(key.getText(), secret.getText());
        reactor.notify(CommandEnum.INFO.getCommandText());
    }

    public GridPane getView() {
        return view;
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setKeysStatus(KeysStatusHolder keysStatusHolder) {
        this.keysStatusHolder = keysStatusHolder;
    }
}
