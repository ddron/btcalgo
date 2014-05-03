package com.btcalgo.ui;

import com.btcalgo.finances.FundsEnum;
import com.btcalgo.ui.model.FinancesInfo;
import com.btcalgo.ui.model.FinancesToShow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.Set;
import java.util.TreeSet;

public class FinancesTab {

    private GridPane view = new GridPane();
    private FinancesToShow financesToShow;

    private TableView<FinancesInfo> tableView = new TableView<>();

    public FinancesTab(FinancesToShow financesToShow) {
        this.financesToShow = financesToShow;

        view.getStylesheets().add(getClass().getResource("/ui/btcealgo.css").toExternalForm());
        view.getStyleClass().add("root");

        VBox basicBox = new VBox();
        basicBox.setMinWidth(400);
        basicBox.setMinHeight(400);
        view.add(basicBox, 0, 0);

        basicBox.getChildren().add(tableView);
        initTableView();

        HBox totalFinances = new HBox();
        basicBox.getChildren().add(totalFinances);
        initTotalFinancesBox(totalFinances);
    }

    private void initTotalFinancesBox(HBox totalFinances) {
        ChoiceBox<String> fundsPairs = new ChoiceBox<>();
        Set<String> funds = new TreeSet<>();
        funds.add(FundsEnum.USD.name());
        funds.add(FundsEnum.EUR.name());
        funds.add(FundsEnum.RUR.name());
        funds.add(FundsEnum.CNH.name());
        funds.add(FundsEnum.GBP.name());
        funds.add(FundsEnum.BTC.name());
        fundsPairs.setItems(FXCollections.observableArrayList(funds));
        totalFinances.getChildren().add(fundsPairs);
        fundsPairs.getSelectionModel().selectedItemProperty().addListener(financesToShow);
        fundsPairs.getSelectionModel().select(FundsEnum.USD.name());

        Label totalFunds = new Label();
        totalFunds.textProperty().bind(financesToShow.totalFinancesProperty());
        totalFinances.getChildren().add(totalFunds);
    }

    private void initTableView() {
        TableColumn<FinancesInfo, String> currency = new TableColumn<>("Currency");
        currency.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFundEnumName());

            }
        });
        currency.setPrefWidth(150);

        TableColumn<FinancesInfo, String> amountOnOrders = new TableColumn<>("Amount on orders");
        amountOnOrders.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFinancesOnOrders());

            }
        });
        amountOnOrders.setPrefWidth(150);

        TableColumn<FinancesInfo, String> amountTotal = new TableColumn<>("Total amount");
        amountTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFinancesTotal());

            }
        });
        amountTotal.setPrefWidth(150);

        tableView.setItems(financesToShow.getDataToShow());
        tableView.getColumns().addAll(currency, amountOnOrders, amountTotal);
    }

    public GridPane getView() {
        return view;
    }

}
