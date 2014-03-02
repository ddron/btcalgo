package com.btcalgo.service.marketdata;

import com.btcalgo.model.BestOrderBook;
import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import reactor.core.Reactor;

public class MockMarketDataProvider extends AbstractMarketDataProvider {

    public MockMarketDataProvider(Reactor reactor) {
        super(reactor);
    }

    public void publishNewMDUpdate(String market, SymbolEnum symbol, double bestBuy, double bestSell) {
        IOrderBook book = new BestOrderBook(market, symbol, bestBuy, bestSell);
        log.debug("MD update received: {}", book);

        String key = buildKey(book.getMarket(), book.getSymbol());
        booksCache.put(key, book);
        notifyListeners(book);
    }

}
