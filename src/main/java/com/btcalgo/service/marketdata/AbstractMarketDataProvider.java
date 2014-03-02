package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;
import reactor.event.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractMarketDataProvider implements IMarketDataProvider {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private Reactor reactor;

    private Map<String, Map<IMarketDataListener, Predicate<IOrderBook>>> listeners = new ConcurrentHashMap<>();
    protected Map<String, IOrderBook> booksCache = new ConcurrentHashMap<>();

    public AbstractMarketDataProvider(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public void addListener(IMarketDataListener listener, String market, SymbolEnum symbol, Predicate<IOrderBook> predicate) {
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
    public void addListener(IMarketDataListener listener, String market, SymbolEnum symbol) {
        addListener(listener, market, symbol, Predicates.<IOrderBook>alwaysTrue());
    }

    @Override
    public IOrderBook getIOrderBook(String market, SymbolEnum symbol) {
        return booksCache.get(buildKey(market, symbol));
    }

    @Override
    public void removeListener(IMarketDataListener listener, String market, SymbolEnum symbol) {
        if (listener == null || Strings.isNullOrEmpty(market) || symbol == null) {
            return;
        }

        String key = buildKey(market, symbol);
        Map<IMarketDataListener, Predicate<IOrderBook>> listenersForKey = listeners.get(key);
        if (listenersForKey != null) {
            listenersForKey.remove(listener);
        }
    }

    protected String buildKey(String market, SymbolEnum symbol) {
        return market + '#' + symbol.name();
    }

    protected void notifyListeners(IOrderBook book) {
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
