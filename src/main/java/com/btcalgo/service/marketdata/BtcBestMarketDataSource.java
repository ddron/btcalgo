package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.TickerService;
import com.btcalgo.service.api.templates.TickerTemplate;
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

    private TickerService tickerService;
    private Reactor reactor;

    public BtcBestMarketDataSource(TickerService tickerService, Reactor reactor) {
        this.tickerService = tickerService;
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
            try {
                TickerTemplate tickerTemplate = tickerService.getTicker(symbol);
                if (tickerTemplate != null) {
                    IOrderBook book = tickerTemplate.convertToBestOrderBook();

                    for (IMarketDataListener listener : listeners) {
                        reactor.notify(listener.getId(), Event.wrap(book));
                    }
                }
            } finally {
                processing.get(symbol).set(false);
            }
        }
    }

    public void setListeners(Set<IMarketDataListener> listeners) {
        this.listeners = new CopyOnWriteArraySet<>(listeners);
    }
}
