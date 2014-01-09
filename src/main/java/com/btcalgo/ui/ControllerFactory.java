package com.btcalgo.ui;

import com.btcalgo.execution.OrdersManager;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.ui.model.KeysStatusHolder;
import com.btcalgo.ui.model.MarketDataToShow;
import javafx.fxml.FXMLLoader;
import reactor.core.Reactor;

import java.io.IOException;
import java.io.InputStream;

public class ControllerFactory {

    private Reactor reactor;
    private ApiService apiService;
    private OrdersManager ordersManager;

    private KeysStatusHolder keysStatusHolder;
    private MarketDataToShow marketDataToShow;

    public SinglePageController createController(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));
            loader.load(fxmlStream);

            SinglePageController controller = loader.getController();
            controller.setReactor(reactor);
            controller.setApiService(apiService);
            controller.setOrdersManager(ordersManager);

            controller.setKeysStatus(keysStatusHolder);
            controller.setMarketDataToShow(marketDataToShow);

            controller.initController();

            return controller;
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }

    public void setReactor(Reactor reactor) {
        this.reactor = reactor;
    }

    public void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public void setKeysStatusHolder(KeysStatusHolder keysStatusHolder) {
        this.keysStatusHolder = keysStatusHolder;
    }

    public void setMarketDataToShow(MarketDataToShow marketDataToShow) {
        this.marketDataToShow = marketDataToShow;
    }

    public void setOrdersManager(OrdersManager ordersManager) {
        this.ordersManager = ordersManager;
    }
}
