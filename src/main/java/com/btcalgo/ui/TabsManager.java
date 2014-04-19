package com.btcalgo.ui;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TabsManager {

    private TradingTab tradingTab;
    private FinancesTab financesTab;

    public void init(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab trade = new Tab();
        trade.setClosable(false);
        trade.setText("Trading");
        trade.setContent(tradingTab.getView());
        tabPane.getTabs().add(trade);

        Tab finances = new Tab();
        finances.setClosable(false);
        finances.setText("My Finances");
        tabPane.getTabs().add(finances);

        Scene scene = new Scene(tabPane, 660, 600);
        scene.getStylesheets().add(
                TabsManager.class.getResource("/ui/btcealgo.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("BtcAlgo");

        tradingTab.initController(stage.getScene().getWindow());
        stage.show();
    }

    public void setTradingTab(TradingTab tradingTab) {
        this.tradingTab = tradingTab;
    }

    public void setFinancesTab(FinancesTab financesTab) {
        this.financesTab = financesTab;
    }
}
