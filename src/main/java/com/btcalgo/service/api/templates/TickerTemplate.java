package com.btcalgo.service.api.templates;

import com.btcalgo.model.BestOrderBook;

public class TickerTemplate {
    private Ticker ticker;

    public BestOrderBook convertToBestOrderBook() {
        return ticker.convertToBestOrderBook();
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    @Override
    public String toString() {
        return ticker.toString();
    }


}
