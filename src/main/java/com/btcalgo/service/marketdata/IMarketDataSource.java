package com.btcalgo.service.marketdata;

import com.btcalgo.reactor.NameableConsumer;
import reactor.event.Event;

public interface IMarketDataSource extends NameableConsumer<Event<SymbolEnum>> {
}
