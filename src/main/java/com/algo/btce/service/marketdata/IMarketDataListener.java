package com.algo.btce.service.marketdata;

import com.algo.btce.model.IOrderBook;
import com.algo.btce.reactor.NameableConsumer;
import reactor.event.Event;

public interface IMarketDataListener extends NameableConsumer<Event<IOrderBook>> {
}
