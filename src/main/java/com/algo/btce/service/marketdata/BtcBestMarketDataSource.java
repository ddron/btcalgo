package com.algo.btce.service.marketdata;

import com.algo.btce.service.api.service.ApiService;
import reactor.core.Reactor;
import reactor.event.Event;

public class BtcBestMarketDataSource implements IMarketDataSource {

    private ApiService apiService;
    private Reactor reactor;

    public BtcBestMarketDataSource(ApiService apiService, Reactor reactor) {
        this.apiService = apiService;
        this.reactor = reactor;
    }

    @Override
    public String getId() {
        return "marketDataSource";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
