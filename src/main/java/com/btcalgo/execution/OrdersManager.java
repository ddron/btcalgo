package com.btcalgo.execution;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.service.marketdata.MarketDataProvider;
import com.btcalgo.service.marketdata.PriceIsWorseOrEqualThanCondition;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OrdersManager {

    /** 'internalOrderId' to 'order' map*/
    private Map<String, Order> orders = new ConcurrentHashMap<>();

    private ApiService apiService;
    private MarketDataProvider marketDataProvider;

    public OrdersManager(ApiService apiService, MarketDataProvider marketDataProvider) {
        this.apiService = apiService;
        this.marketDataProvider = marketDataProvider;
    }

    public void createNew(OrderDataHolder holder) {
        // create new order from holder object
        Order order = Order.OrderBuilder.newOrder()
                .setApiService(apiService)
                .setMarketDataProvider(marketDataProvider)
                .setDirection(holder.getDirection())
                .setSymbol(holder.getSymbol())
                .setMarket(holder.getMarket())
                .setStrategyType(holder.getStrategyType())
                .setAmount(holder.getAmount())
                .setStopPrice(holder.getStopPrice())
                .setLimitPrice(holder.getLimitPrice())
                .build();


        // register new order as market data listener
        Predicate<IOrderBook> condition =
                new PriceIsWorseOrEqualThanCondition(order.getStopPrice(), order.getDirection());
        marketDataProvider.addListener(order, order.getMarket(), order.getSymbol(), condition);

        // store reference to internal map
        orders.put(order.getInternalOrderId(), order);
    }

    public void cancel(String internalOrderId) {
        Order order = orders.get(internalOrderId);
        if (order != null) {
            if (order.getStatusReference().compareAndSet(OrderStatus.WAITING, OrderStatus.CANCELLED)) {
                marketDataProvider.removeListener(order, order.getMarket(), order.getSymbol());
            }
        }
    }

    public void remove(String internalOrderId) {
        Order order = orders.get(internalOrderId);
        if (order != null) {
            if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.SENT) {
                orders.remove(internalOrderId);
            }
        }
    }

}
