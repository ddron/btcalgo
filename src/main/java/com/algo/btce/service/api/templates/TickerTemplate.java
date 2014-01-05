package com.algo.btce.service.api.templates;

import com.algo.btce.model.BestOrderBook;

public class TickerTemplate {
    Ticker ticker;

    public BestOrderBook convertToBestOrderBook() {
        return ticker.convertToBestOrderBook();
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }
}
