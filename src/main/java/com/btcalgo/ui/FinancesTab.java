package com.btcalgo.ui;

import javafx.scene.layout.GridPane;

public class FinancesTab {

    private GridPane view = new GridPane();

    public FinancesTab() {
        view.getStylesheets().add(getClass().getResource("/ui/btcealgo.css").toExternalForm());
        view.getStyleClass().add("root");

    }

    public GridPane getView() {
        return view;
    }

}
