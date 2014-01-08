package com.btcalgo.ui;

import com.btcalgo.CommandEnum;
import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.ui.model.KeysStatusHolder;
import com.btcalgo.ui.model.MarketDataToShow;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import reactor.core.Reactor;

public class SinglePageController {

    @FXML private GridPane view;

    @FXML private TextField key;
    @FXML private PasswordField secret;
    @FXML private Text keysStatus;
    @FXML private Button validate;

    @FXML private ChoiceBox<String> pairs;
    @FXML private TextField bestBuy;
    @FXML private TextField bestSell;

    @FXML private ChoiceBox<String> strategyTypes;
    @FXML private TextField stopPrice;
    @FXML private TextField limitPrice;
    @FXML private Button submit;

    @FXML private TableView ordersView;

    private Reactor reactor;
    private ApiService apiService;
    private KeysStatusHolder keysStatusHolder;
    private MarketDataToShow marketDataToShow;

    public void initController() {
        keysStatus.textProperty().bind(keysStatusHolder.keysStatusProperty());
        validate.disableProperty().bind(keysStatusHolder.validateBtnDisabledProperty());

        bestBuy.textProperty().bind(marketDataToShow.bestBidPriceProperty());
        bestSell.textProperty().bind(marketDataToShow.bestAskPriceProperty());

        pairs.setItems(FXCollections.<String>observableArrayList(SymbolEnum.getDisplayNames()));
        pairs.getSelectionModel().selectedItemProperty().addListener(marketDataToShow);
        pairs.getSelectionModel().selectFirst();

        strategyTypes.setItems(FXCollections.<String>observableArrayList(StrategyType.getDisplayNames()));
        strategyTypes.getSelectionModel().selectFirst();

    }

    @SuppressWarnings("UnusedParameters")
    public void handleValidateOnClick(ActionEvent actionEvent) {
        keysStatusHolder.setValidateBtnDisabled(true);
        keysStatusHolder.setValidatingStatus();

        apiService.updateKeys(key.getText(), secret.getText());
        reactor.notify(CommandEnum.INFO.getCommandText());
    }

    public void handleSubmit(ActionEvent actionEvent) {

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

    public void setMarketDataToShow(MarketDataToShow marketDataToShow) {
        this.marketDataToShow = marketDataToShow;
    }
}
