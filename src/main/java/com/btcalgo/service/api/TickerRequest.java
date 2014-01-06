package com.btcalgo.service.api;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.TickerTemplate;
import com.btcalgo.service.marketdata.SymbolEnum;
import reactor.event.Event;

public class TickerRequest implements NameableConsumer<Event<Void>> {

    private ApiService apiService;

    public TickerRequest(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public String getId() {
        return "ticker";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public TickerTemplate getTicker(SymbolEnum symbol) {
        TickerTemplate tickerTemplate = apiService.getTicker(symbol.getValue());
        tickerTemplate.getTicker().setSymbol(symbol.getValue());

        return tickerTemplate;
    }
}

