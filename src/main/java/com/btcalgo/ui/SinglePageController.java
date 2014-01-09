package com.btcalgo.ui;

import com.btcalgo.CommandEnum;
import com.btcalgo.execution.OrdersManager;
import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.ui.model.KeysStatusHolder;
import com.btcalgo.ui.model.MarketDataToShow;
import com.btcalgo.ui.model.OrderDataHolder;
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
    @FXML private ChoiceBox<String> direction;

    @FXML private TextField stopPrice;
    @FXML private TextField limitPrice;
    @FXML private TextField amount;
    @FXML private Button submit;

    @FXML private TableView ordersView;

    private Reactor reactor;
    private ApiService apiService;
    private OrdersManager ordersManager;

    private KeysStatusHolder keysStatusHolder;
    private MarketDataToShow marketDataToShow;

    public void initController() {
        // keys
        keysStatus.textProperty().bind(keysStatusHolder.keysStatusProperty());
        validate.disableProperty().bind(keysStatusHolder.validateBtnDisabledProperty());

        // market data
        bestBuy.textProperty().bind(marketDataToShow.bestBidPriceProperty());
        bestSell.textProperty().bind(marketDataToShow.bestAskPriceProperty());

        // select symbols and bind with market data
        pairs.setItems(FXCollections.<String>observableArrayList(SymbolEnum.getDisplayNames()));
        pairs.getSelectionModel().selectedItemProperty().addListener(marketDataToShow);
        pairs.getSelectionModel().selectFirst();

        // strategy types
        strategyTypes.setItems(FXCollections.<String>observableArrayList(StrategyType.getDisplayNames()));
        strategyTypes.getSelectionModel().selectFirst();

        // direction (i.e. side)
        direction.setItems(FXCollections.<String>observableArrayList(Direction.getDisplayNames()));
        direction.getSelectionModel().selectFirst();
    }

    @SuppressWarnings("UnusedParameters")
    public void handleValidateOnClick(ActionEvent actionEvent) {
        keysStatusHolder.setValidateBtnDisabled(true);
        keysStatusHolder.setValidatingStatus();

        apiService.updateKeys(key.getText(), secret.getText());
        reactor.notify(CommandEnum.INFO.getCommandText());
    }

    public void handleSubmit(ActionEvent actionEvent) {
        submit.setDisable(true);

        ValidationErrors errors = validateOrderFields();
        if (errors.isEmpty()) {
            OrderDataHolder orderDataHolder = OrderDataHolder.OrderDataHolderBuilder.newOrderDataHolder()
                    .setDirection(direction.getSelectionModel().getSelectedItem())
                    .setSymbol(pairs.getSelectionModel().getSelectedItem())
                    .setStrategyType(strategyTypes.getSelectionModel().getSelectedItem())
                    .setAmount(amount.getText())
                    .setStopPrice(stopPrice.getText())
                    .setLimitPrice(limitPrice.getText())
                    .build();

            ordersManager.createNew(orderDataHolder);

            // clear prices and amount fields
            stopPrice.clear();
            limitPrice.clear();
            amount.clear();
        } else {
            // TODO: implement 'else' case (display validation errors in pop up window)
        }

        submit.setDisable(false);
    }

    private ValidationErrors validateOrderFields() {
        // TODO: do implement
        // TODO: check keys validity first
        // TODO: check fields validity only in case of having valid keys
        return new ValidationErrors();
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

    public void setOrdersManager(OrdersManager ordersManager) {
        this.ordersManager = ordersManager;
    }
}
