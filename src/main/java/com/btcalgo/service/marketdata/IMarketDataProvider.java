package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import com.google.common.base.Predicate;

public interface IMarketDataProvider {

    void addListener(IMarketDataListener listener, String market, SymbolEnum symbol, Predicate<IOrderBook> predicate);

    void addListener(IMarketDataListener listener, String market, SymbolEnum symbol);

    void removeListener(IMarketDataListener listener, String market, SymbolEnum symbol);

    IOrderBook getIOrderBook(String market, SymbolEnum symbol);
}
