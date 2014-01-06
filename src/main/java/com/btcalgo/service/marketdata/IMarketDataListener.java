package com.btcalgo.service.marketdata;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.reactor.NameableConsumer;
import reactor.event.Event;

public interface IMarketDataListener extends NameableConsumer<Event<IOrderBook>> {
}
