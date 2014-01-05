package com.algo.btce.service.marketdata;

import com.algo.btce.reactor.NameableConsumer;
import reactor.event.Event;

public interface IMarketDataSource extends NameableConsumer<Event<SymbolEnum>> {
}
