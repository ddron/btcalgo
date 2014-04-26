package com.btcalgo.ui;

import com.btcalgo.ui.model.FinancesInfo;
import com.btcalgo.ui.model.FinancesToShow;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FinancesTab {

    private GridPane view = new GridPane();
    private FinancesToShow financesToShow;

    private TableView<FinancesInfo> tableView = new TableView<>();

    public FinancesTab(FinancesToShow financesToShow) {
        this.financesToShow = financesToShow;

        view.getStylesheets().add(getClass().getResource("/ui/btcealgo.css").toExternalForm());
        view.getStyleClass().add("root");

        VBox vBox = new VBox();
        vBox.setMinWidth(400);
        vBox.setMinHeight(400);
        view.add(vBox, 0, 0);

        vBox.getChildren().add(tableView);
        initTableView();
    }

    private void initTableView() {
        TableColumn<FinancesInfo, String> currency = new TableColumn<>("Currency");
        currency.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFundEnumName());

            }
        });
        currency.setPrefWidth(70);

        TableColumn<FinancesInfo, String> amountOnOrders = new TableColumn<>("Amount on orders");
        amountOnOrders.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFinancesOnOrders());

            }
        });
        amountOnOrders.setPrefWidth(70);

        TableColumn<FinancesInfo, String> amountTotal = new TableColumn<>("Total amount");
        amountTotal.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FinancesInfo, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<FinancesInfo, String> p) {
                return new SimpleStringProperty(p.getValue().getFinancesTotal());

            }
        });
        amountTotal.setPrefWidth(70);

        tableView.setItems(financesToShow.getDataToShow());
        tableView.getColumns().addAll(currency, amountOnOrders, amountTotal);
    }

    public GridPane getView() {
        return view;
    }

}
