package com.btcalgo.service.api;

import com.btcalgo.model.SymbolEnum;
import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.TickerTemplate;
import reactor.event.Event;

public class TickerService implements NameableConsumer<Event<Void>> {

    private ApiService apiService;

    public TickerService(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public String getId() {
        return "ticker";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
    }

    public TickerTemplate getTicker(SymbolEnum symbol) {
        TickerTemplate tickerTemplate = apiService.getTicker(symbol.getValue());

        if (tickerTemplate == null) {
            return null;
        } else {
            tickerTemplate.getTicker().setSymbol(symbol);
            return tickerTemplate;
        }

    }
}

