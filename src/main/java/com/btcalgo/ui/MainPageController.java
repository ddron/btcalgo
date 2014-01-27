package com.btcalgo.ui;

import com.btcalgo.CommandEnum;
import com.btcalgo.execution.Action;
import com.btcalgo.execution.Order;
import com.btcalgo.execution.OrdersManager;
import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.ui.model.KeysStatusHolder;
import com.btcalgo.ui.model.MarketDataToShow;
import com.btcalgo.ui.model.OrderDataHolder;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import reactor.core.Reactor;

import java.util.List;

public class MainPageController {

    @FXML private GridPane view;

    // credentials
    @FXML private TextField key;
    @FXML private PasswordField secret;
    @FXML private Text keysStatus;
    @FXML private Button validate;
    private KeysStatusHolder keysStatusHolder;

    // market data
    @FXML private ChoiceBox<String> pairs;
    @FXML private Label bestBuy;
    @FXML private Label bestSell;
    private MarketDataToShow marketDataToShow;

    // place an order
    @FXML private ChoiceBox<String> strategyTypes;
    @FXML private ChoiceBox<String> direction;
    @FXML private Label symbol;
    @FXML private TextField amount;
    @FXML private TextField stopPrice;
    @FXML private TextField limitPrice;
    @FXML private Button submit;

    // orders
    @FXML private TableView<Order> ordersView;

    private ValidationController validationController;
    private LicenseController licenseController;

    private Reactor reactor;
    private ApiService apiService;
    private OrdersManager ordersManager;

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
        symbol.textProperty().bind(pairs.getSelectionModel().selectedItemProperty());

        // strategy types
        strategyTypes.setItems(FXCollections.<String>observableArrayList(StrategyType.getDisplayNames()));
        strategyTypes.getSelectionModel().selectFirst();

        // direction (i.e. side)
        direction.setItems(FXCollections.<String>observableArrayList(Direction.getDisplayNames()));
        direction.getSelectionModel().selectFirst();

        initOrdersViewTable();
        validationController.initValidationPopup(this);
        licenseController.initLicensePopup(this);
    }

    private void initOrdersViewTable() {
        TableColumn<Order, String> typeCol = new TableColumn<>("Order Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<Order, String>("strategyTypeAsString"));

        TableColumn<Order, String> directionCol = new TableColumn<>("Side");
        directionCol.setCellValueFactory(new PropertyValueFactory<Order, String>("directionAsString"));
        directionCol.setPrefWidth(35);

        TableColumn<Order, String> symbolCol = new TableColumn<>("Currency");
        symbolCol.setCellValueFactory(new PropertyValueFactory<Order, String>("symbolAsString"));
        symbolCol.setPrefWidth(70);

        TableColumn<Order, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(new PropertyValueFactory<Order, String>("amount"));
        amountCol.setPrefWidth(70);

        TableColumn<Order, String> stopPriceCol = new TableColumn<>("Stop Price");
        stopPriceCol.setCellValueFactory(new PropertyValueFactory<Order, String>("stopPrice"));
        stopPriceCol.setPrefWidth(70);

        TableColumn<Order, String> limitPriceCol = new TableColumn<>("Price");
        limitPriceCol.setCellValueFactory(new PropertyValueFactory<Order, String>("limitPrice"));
        limitPriceCol.setPrefWidth(70);

        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<Order, String>("displayStatus"));
        statusCol.setPrefWidth(70);

        //Insert Button
        TableColumn<Order, String> actionCol = new TableColumn<>("Action");
        actionCol.setSortable(false);
        actionCol.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                        return p.getValue().validActionProperty();
                    }
                });
        actionCol.setCellFactory(
                new Callback<TableColumn<Order, String>, TableCell<Order, String>>() {
                    @Override
                    public TableCell<Order, String> call(TableColumn<Order, String> p) {
                        return new ButtonCell();
                    }
                });
        actionCol.setPrefWidth(70);

        ordersView.setItems(ordersManager.getOrdersView());
        ordersView.getColumns().addAll(typeCol, directionCol, symbolCol,
                amountCol, stopPriceCol, limitPriceCol, statusCol, actionCol);
        ordersView.setPlaceholder(new Label("Your orders will be displayed here"));
    }

    public void handleLicensing(ActionEvent actionEvent) {
        licenseController.showLicensePopup();
    }

    private class ButtonCell extends TableCell<Order, String> {
        final Button cellButton = new Button(Action.CANCEL.name());

        ButtonCell(){
            cellButton.textProperty().bind(itemProperty());

            cellButton.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent t) {
                    String id = ((Order) getTableRow().getItem()).getId();
                    Action action = Action.valueOf(getItem());
                    if (action == Action.CANCEL) {
                        ordersManager.cancel(id);
                    } else if (action == Action.REMOVE) {
                        ordersManager.remove(id);
                    }
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void handleValidate(ActionEvent actionEvent) {
        List<String> errors = validationController.validateKeys(this);

        if (errors.isEmpty()) {
            keysStatusHolder.setValidateBtnDisabled(true);
            keysStatusHolder.setValidatingStatus();

            apiService.updateKeys(key.getText(), secret.getText());
            reactor.notify(CommandEnum.INFO.getCommandText());
        } else {
            validationController.showValidationPopup(errors);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void handleSubmit(ActionEvent actionEvent) {
        submit.setDisable(true);

        List<String> errors = validationController.validateOrderFields(this);
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
            validationController.showValidationPopup(errors);
        }

        submit.setDisable(false);
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

    public void setValidationController(ValidationController validationController) {
        this.validationController = validationController;
    }

    public void setLicenseController(LicenseController licenseController) {
        this.licenseController = licenseController;
    }

    public TextField getAmount() {
        return amount;
    }

    public TextField getStopPrice() {
        return stopPrice;
    }

    public TextField getLimitPrice() {
        return limitPrice;
    }

    public TextField getKey() {
        return key;
    }

    public PasswordField getSecret() {
        return secret;
    }

    public ChoiceBox<String> getDirection() {
        return direction;
    }

    public ChoiceBox<String> getStrategyTypes() {
        return strategyTypes;
    }
}
