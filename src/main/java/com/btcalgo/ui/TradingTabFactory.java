package com.btcalgo.ui;

import com.btcalgo.execution.OrdersManager;
import com.btcalgo.service.RuntimeMeter;
import com.btcalgo.service.api.IApiService;
import com.btcalgo.ui.model.MarketDataToShow;
import javafx.fxml.FXMLLoader;
import reactor.core.Reactor;

import java.io.IOException;
import java.io.InputStream;

public class TradingTabFactory {

    private Reactor reactor;
    private IApiService apiService;
    private OrdersManager ordersManager;

    private KeysController keysController;
    private MarketDataToShow marketDataToShow;

    private ValidationController validationController;
    private LicenseController licenseController;

    private RuntimeMeter runtimeMeter;

    public TradingTab createTradingTab(String url) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(url));
            loader.load(fxmlStream);

            TradingTab controller = loader.getController();
            controller.setReactor(reactor);
            controller.setApiService(apiService);
            controller.setOrdersManager(ordersManager);

            controller.setValidationController(validationController);
            controller.setLicenseController(licenseController);

            controller.setKeysController(keysController);
            controller.setMarketDataToShow(marketDataToShow);

            controller.setRuntimeMeter(runtimeMeter);

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

    public void setApiService(IApiService apiService) {
        this.apiService = apiService;
    }

    public void setKeysController(KeysController keysController) {
        this.keysController = keysController;
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

    public void setRuntimeMeter(RuntimeMeter runtimeMeter) {
        this.runtimeMeter = runtimeMeter;
    }
}
