package com.btcalgo.ui;

import com.btcalgo.finances.FundsEnum;
import com.btcalgo.ui.model.FinancesInfo;
import com.btcalgo.ui.model.FinancesToShow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
        basicBox.setSpacing(10);
        view.add(basicBox, 0, 0);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        basicBox.getChildren().add(hBox);
        VBox top = new VBox();
        top.setMaxWidth(500);
        hBox.getChildren().add(top);
        top.getStyleClass().add("block");
        top.setPadding(new Insets(10, 10, 10, 10));

        HBox totalFinances_1 = new HBox();
        top.getChildren().add(totalFinances_1);
        initTotalFinances_1(totalFinances_1);

        HBox totalFinances_2 = new HBox();
        top.getChildren().add(totalFinances_2);
        initTotalFinances_2(totalFinances_2);

        VBox tableBox = new VBox();
        basicBox.getChildren().add(tableBox);
        tableBox.getStyleClass().add("block");
        tableBox.setPadding(new Insets(10, 10, 10, 10));

        tableBox.getChildren().add(tableView);
        initTableView();
    }

    /**
     * looks like:<br></br>
     * Total finances in EUR
     */
    private void initTotalFinances_1(HBox totalFinances_1) {
        totalFinances_1.setAlignment(Pos.CENTER);
        totalFinances_1.setSpacing(10);

        Label label = new Label("Your total finances in");
        totalFinances_1.getChildren().add(label);

        ChoiceBox<String> fundsPairs = new ChoiceBox<>();
        Set<String> funds = new TreeSet<>();
        funds.add(FundsEnum.USD.name());
        funds.add(FundsEnum.EUR.name());
        funds.add(FundsEnum.RUR.name());
        funds.add(FundsEnum.CNH.name());
        funds.add(FundsEnum.GBP.name());
        funds.add(FundsEnum.BTC.name());
        fundsPairs.setItems(FXCollections.observableArrayList(funds));

        totalFinances_1.getChildren().add(fundsPairs);
        fundsPairs.getSelectionModel().selectedItemProperty().addListener(financesToShow);
        fundsPairs.getSelectionModel().select(FundsEnum.USD.name());
    }

    private void initTotalFinances_2(HBox totalFinances_2) {
        totalFinances_2.setAlignment(Pos.CENTER);
        totalFinances_2.setPadding(new Insets(10, 10, 10, 10));

        Label totalFunds = new Label();
        totalFunds.getStyleClass().add("total_finances");
        totalFunds.textProperty().bind(financesToShow.totalFinancesProperty());
        totalFinances_2.getChildren().add(totalFunds);
    }

    private void initTableView() {
        TableColumn<FinancesInfo, String> currency = new TableColumn<>("Currency");
        currency.setCellFactory(new Callback<TableColumn<FinancesInfo, String>, TableCell<FinancesInfo, String>>() {
            @Override
            public TableCell<FinancesInfo, String> call(TableColumn<FinancesInfo, String> p) {
                TableCell<FinancesInfo, String> tc = new TableCell<FinancesInfo, String>(){
                    @Override
                    public void updateItem(String item, boolean empty) {
                        if (item != null){
                            setText(item);
                        }
                    }
                };
                tc.setAlignment(Pos.CENTER);
                return tc;
            }
        });
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
        tableView.setPlaceholder(new Label("Your finances will be shown here after providing valid API keys"));
    }

    public GridPane getView() {
        return view;
    }

}
