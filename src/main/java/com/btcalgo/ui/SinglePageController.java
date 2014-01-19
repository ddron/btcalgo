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
import com.btcalgo.util.StringUtils;
import com.google.common.base.Strings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import reactor.core.Reactor;

import java.util.ArrayList;
import java.util.List;

import static com.btcalgo.ui.ValidationErrors.ErrorType.*;
import static com.btcalgo.ui.ValidationErrors.Field.*;
import static com.btcalgo.ui.ValidationErrors.getErrorValue;

public class SinglePageController {

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

    private Stage popup;
    private VBox errorsContent;

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

        // init orders view table
        initOrdersViewTable();

        // init popup
        initPopup();
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

    private void initPopup() {
        popup = new Stage();
        popup.initOwner(view.getScene().getWindow());
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
                SinglePageController.class.getResource("/ui/btcealgo.css").toExternalForm());
        popup.setScene(popupScene);
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
        List<String> errors = validateKeys();

        if (errors.isEmpty()) {
            keysStatusHolder.setValidateBtnDisabled(true);
            keysStatusHolder.setValidatingStatus();

            apiService.updateKeys(key.getText(), secret.getText());
            reactor.notify(CommandEnum.INFO.getCommandText());
        } else {
            showPopupWindow(errors);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void handleSubmit(ActionEvent actionEvent) {
        submit.setDisable(true);

        List<String> errors = validateOrderFields();
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
            showPopupWindow(errors);
        }

        submit.setDisable(false);
    }

    private void showPopupWindow(List<String> errors) {
        errorsContent.getChildren().clear();
        for (String error : errors) {
            errorsContent.getChildren().add(new Label(error));
        }
        popup.show();
    }

    private List<String> validateOrderFields() {
        List<String> result = validateKeys();
        if (!result.isEmpty()) {
            return result;
        }

        // check strategy type
        if (StrategyType.valueByDisplayName(strategyTypes.getSelectionModel().getSelectedItem()) != StrategyType.STOP_LOSS) {
            result.add(ValidationErrors.getErrorValue(STRATEGY_TYPE, INCORRECT_VALUE));
        }

        // check side
        if (Direction.valueByDisplayName(direction.getSelectionModel().getSelectedItem()) == Direction.NONE) {
            result.add(ValidationErrors.getErrorValue(DIRECTION, INCORRECT_VALUE));
        }

        // check amount
        String amountValue = amount.getText();
        if (Strings.isNullOrEmpty(amountValue)) {
            result.add(ValidationErrors.getErrorValue(AMOUNT, EMPTY));
        } else if (!StringUtils.isNumber(amountValue)) {
            result.add(ValidationErrors.getErrorValue(AMOUNT, FORMAT));
        }

        // stop price
        String stopPriceValue = stopPrice.getText();
        if (Strings.isNullOrEmpty(stopPriceValue)) {
            result.add(ValidationErrors.getErrorValue(STOP_PRICE, EMPTY));
        } else if (!StringUtils.isNumber(stopPriceValue)) {
            result.add(ValidationErrors.getErrorValue(STOP_PRICE, FORMAT));
        }

        // limit price
        String limitPriceValue = limitPrice.getText();
        if (Strings.isNullOrEmpty(limitPriceValue)) {
            result.add(ValidationErrors.getErrorValue(LIMIT_PRICE, EMPTY));
        } else if (!StringUtils.isNumber(limitPriceValue)) {
            result.add(ValidationErrors.getErrorValue(LIMIT_PRICE, FORMAT));
        }

        return result;
    }

    private List<String> validateKeys() {
        List<String> result = new ArrayList<>();
        if (Strings.isNullOrEmpty(key.getText())) {
            result.add(getErrorValue(KEY, EMPTY));
        }
        if (Strings.isNullOrEmpty(secret.getText())) {
            result.add(getErrorValue(SECRET, EMPTY));
        }
        return result;
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
