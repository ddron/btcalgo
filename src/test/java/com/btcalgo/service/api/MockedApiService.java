package com.btcalgo.service.api;

import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.service.api.templates.NewOrderTemplate;
import com.btcalgo.service.api.templates.Ticker;
import com.btcalgo.service.api.templates.TickerTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO: add methods implementation
public class MockedApiService implements IApiService {

    private volatile boolean validKeys = false;

    private Map<String, TickerTemplate> tickers = new ConcurrentHashMap<>();

    @Override
    public void updateKeys(String key, String secret) {
        // TODO: do we need to implement it?
    }

    @Override
    public boolean hasValidKeys() {
        return validKeys;
    }

    @Override
    public void setValidKeys(boolean validKeys) {
        this.validKeys = validKeys;
    }

    @Override
    public TickerTemplate getTicker(String symbol) {
        return tickers.get(symbol);
    }

    public void setNewTicker(String pair, String buyPrice, String sellPrice) {
        Ticker ticker = new Ticker();
        ticker.setSymbol(SymbolEnum.valueOf(pair));
        ticker.setBuy(Double.valueOf(buyPrice));
        ticker.setSell(Double.valueOf(sellPrice));

        TickerTemplate tickerTemplate = new TickerTemplate();
        tickerTemplate.setTicker(ticker);

        tickers.put(pair, tickerTemplate);
    }

    @Override
    public NewOrderTemplate sendNewOrder(SymbolEnum symbol, Direction direction, double price, double amount) {
        // TODO: implement it
        return null;
    }

    @Override
    public InfoTemplate getInfo() {
        // TODO: do we need to implement it?
        return null;
    }
}
