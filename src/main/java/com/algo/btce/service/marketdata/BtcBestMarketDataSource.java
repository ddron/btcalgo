package com.algo.btce.service.marketdata;

import com.algo.btce.model.IOrderBook;
import com.algo.btce.service.api.service.TickerRequest;
import com.algo.btce.service.api.templates.TickerTemplate;
import reactor.core.Reactor;
import reactor.event.Event;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

public class BtcBestMarketDataSource implements IMarketDataSource {

    private Set<IMarketDataListener> listeners = new CopyOnWriteArraySet<>();
    private Map<SymbolEnum, AtomicBoolean> processing = new ConcurrentHashMap<>();

    private TickerRequest tickerRequest;
    private Reactor reactor;

    public BtcBestMarketDataSource(TickerRequest tickerRequest, Reactor reactor) {
        this.tickerRequest = tickerRequest;
        this.reactor = reactor;

        for (SymbolEnum symbolEnum : SymbolEnum.values()) {
            processing.put(symbolEnum, new AtomicBoolean(false));
        }
    }

    @Override
    public String getId() {
        return "btcBestMarketDataSource";
    }

    @Override
    public void accept(Event<SymbolEnum> symbolEnumEvent) {
        SymbolEnum symbol = symbolEnumEvent.getData();

        // we do not need to request MD update if we're already in progress for the same symbol
        if (processing.get(symbol).compareAndSet(false, true)) {
            TickerTemplate tickerTemplate = tickerRequest.getTicker(symbol);
            IOrderBook book = tickerTemplate.convertToBestOrderBook();

            for (IMarketDataListener listener : listeners) {
                reactor.notify(listener.getId(), Event.wrap(book));
            }

            processing.get(symbol).set(false);
        }
    }

    public void setListeners(Set<IMarketDataListener> listeners) {
        this.listeners = new CopyOnWriteArraySet<>(listeners);
    }
}
