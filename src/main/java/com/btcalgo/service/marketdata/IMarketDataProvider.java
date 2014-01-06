package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import com.google.common.base.Predicate;

public interface IMarketDataProvider {

    void addListener(IMarketDataListener listener, String market, String symbol, Predicate<IOrderBook> predicate);

    void addListener(IMarketDataListener listener, String market, String symbol);

    IOrderBook getIOrderBook(String market, String symbol);
}
