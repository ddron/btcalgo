package com.btcalgo.service.api;

import com.btcalgo.execution.Order;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedApiService implements IApiService {

    private Logger log = LoggerFactory.getLogger(getClass());

    private volatile boolean validKeys = false;

    private Map<String, TickerTemplate> tickers = new ConcurrentHashMap<>();
    private BlockingQueue<Order> sentOrders = new ArrayBlockingQueue<>(10);

    @Override
    public void updateKeys(String key, String secret) {
        // TODO: do we need to implement it?
    }

    @Override
    public boolean hasValidKeys() {
        return validKeys;
    }

    @Override
    public void setValidKeys(boolean validKeys) {
        this.validKeys = validKeys;
    }

    @Override
    public TickerTemplate getTicker(String symbol) {
        return tickers.get(symbol);
    }

    public void setNewTicker(String pair, String buyPrice, String sellPrice) {
        Ticker ticker = new Ticker();
        SymbolEnum symbol = SymbolEnum.valueOf(pair);
        ticker.setSymbol(symbol);
        ticker.setBuy(Double.valueOf(buyPrice));
        ticker.setSell(Double.valueOf(sellPrice));

        TickerTemplate tickerTemplate = new TickerTemplate();
        tickerTemplate.setTicker(ticker);

        tickers.put(symbol.getValue(), tickerTemplate);
    }

    @Override
    public NewOrderTemplate sendNewOrder(Order order) {
        log.info("New order received: {}", order);
        sentOrders.add(order);
        NewOrderTemplate newOrderTemplate = mock(NewOrderTemplate.class);
        when(newOrderTemplate.isSuccess()).thenReturn(true);
        return newOrderTemplate;
    }

    @Override
    public InfoTemplate getInfo() {
        // TODO: do we need to implement it?
        return null;
    }

    @Override
    public ActiveOrdersTemplate getActiveOrders() {
        // TODO: do we need to implement it?
        return null;
    }

    public BlockingQueue<Order> getSentOrders() {
        return sentOrders;
    }
}
