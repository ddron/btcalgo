package com.algo.btce.service.marketdata;

import com.algo.btce.model.IOrderBook;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;
import reactor.event.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MarketDataProvider implements IMarketDataProvider, IMarketDataListener {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Reactor reactor;

    private Map<String, Map<IMarketDataListener, Predicate<IOrderBook>>> listeners = new ConcurrentHashMap<>();
    private Map<String, IOrderBook> booksCache = new ConcurrentHashMap<>();

    public MarketDataProvider(Reactor reactor) {
        this.reactor = reactor;
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

    @Override
    public void addListener(IMarketDataListener listener, String market, String symbol, Predicate<IOrderBook> predicate) {
        String key = buildKey(market, symbol);
        Map<IMarketDataListener, Predicate<IOrderBook>> listenersForKey = listeners.get(key);

        if (listenersForKey == null) {
            listenersForKey = new ConcurrentHashMap<>();
            Map<IMarketDataListener, Predicate<IOrderBook>> previousListenersForKey = listeners.put(key, listenersForKey);
            if (previousListenersForKey != null) {
                listenersForKey = previousListenersForKey;
            }
        }

        listenersForKey.put(listener, predicate);
    }

    @Override
    public void addListener(IMarketDataListener listener, String market, String symbol) {
        addListener(listener, market, symbol, Predicates.<IOrderBook>alwaysTrue());
    }

    @Override
    public IOrderBook getIOrderBook(String market, String symbol) {
        return booksCache.get(buildKey(market, symbol));
    }

    private String buildKey(String market, String symbol) {
        return market + '#' + symbol;
    }


    private void notifyListeners(IOrderBook book) {
        String key = buildKey(book.getMarket(), book.getSymbol());
        Map<IMarketDataListener, Predicate<IOrderBook>> listenersForKey = listeners.get(key);

        if (listenersForKey != null) {
            for (Map.Entry<IMarketDataListener, Predicate<IOrderBook>> entry : listenersForKey.entrySet()) {
                IMarketDataListener listener = entry.getKey();
                Predicate<IOrderBook> notifyCondition = entry.getValue();

                if (notifyCondition.apply(book)) {
                    reactor.notify(listener.getId(), Event.wrap(book));
                }
            }
        }
    }
}
