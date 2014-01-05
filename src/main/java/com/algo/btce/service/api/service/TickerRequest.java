package com.algo.btce.service.api.service;

import com.algo.btce.reactor.NameableConsumer;
import com.algo.btce.service.api.templates.TickerTemplate;
import reactor.event.Event;

import static com.algo.btce.service.InstrumentService.SymbolEnum;

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

