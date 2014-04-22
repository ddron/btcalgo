package com.btcalgo.ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TabsManager {

    private TradingTab tradingTab;
    private FinancesTab financesTab;

    public void init(Stage stage) {
        GridPane gridPane = new GridPane();

        Scene scene = new Scene(gridPane, 660, 600);
        scene.getStylesheets().add(getClass().getResource("/ui/background.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("BtcAlgo");

        MenuBar menuBar = createMenuBar();
        gridPane.add(menuBar, 0, 0);

        TabPane tabPane = new TabPane();
        gridPane.add(tabPane, 0, 1);

        Tab trade = new Tab();
        trade.setClosable(false);
        trade.setText("Trading");
        trade.setContent(tradingTab.getView());
        tabPane.getTabs().add(trade);

        Tab finances = new Tab();
        finances.setClosable(false);
        finances.setText("My Finances");
        finances.setContent(financesTab.getView());
        tabPane.getTabs().add(finances);

        tradingTab.initController(stage.getScene().getWindow());
        stage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setPrefWidth(10000);
        menuBar.getStyleClass().add("menu");

        Menu btcAlgo = new Menu("BtcAlgo");
        MenuItem licensing = new MenuItem("BtcAlgo Licensing");
        licensing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tradingTab.handleLicensing();
            }
        });

        btcAlgo.getItems().add(licensing);
        menuBar.getMenus().add(btcAlgo);

        return menuBar;
    }

    public void setTradingTab(TradingTab tradingTab) {
        this.tradingTab = tradingTab;
    }

    public void setFinancesTab(FinancesTab financesTab) {
        this.financesTab = financesTab;
    }
}
