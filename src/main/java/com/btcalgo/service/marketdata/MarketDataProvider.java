package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import reactor.core.Reactor;
import reactor.event.Event;

public class MarketDataProvider extends AbstractMarketDataProvider implements IMarketDataListener {

    public MarketDataProvider(Reactor reactor) {
        super(reactor);
    }

    @Override
    public String getId() {
        return "marketDataProvider";
    }

    @Override
    public void accept(Event<IOrderBook> iOrderBookEvent) {
        IOrderBook book = iOrderBookEvent.getData();
        log.debug("MD update received: {}", book);

        String key = buildKey(book.getMarket(), book.getSymbol());
        booksCache.put(key, book);
        notifyListeners(book);
    }

}
