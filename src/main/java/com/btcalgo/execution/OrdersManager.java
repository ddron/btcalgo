package com.btcalgo.execution;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.service.api.IApiService;
import com.btcalgo.service.marketdata.IMarketDataProvider;
import com.btcalgo.service.marketdata.PriceIsWorseOrEqualThanCondition;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import reactor.core.Reactor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static reactor.event.selector.Selectors.$;

public class OrdersManager {

    /** 'internalOrderId' to 'order' map*/
    private Map<String, Order> orders = new ConcurrentHashMap<>();
    private ObservableList<Order> ordersView = FXCollections.observableArrayList(orders.values());

    private IApiService apiService;
    private IMarketDataProvider marketDataProvider;
    private Reactor reactor;

    public OrdersManager(IApiService apiService, IMarketDataProvider marketDataProvider, Reactor reactor) {
        this.apiService = apiService;
        this.marketDataProvider = marketDataProvider;
        this.reactor = reactor;
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


        // store reference to internal map
        orders.put(order.getInternalOrderId(), order);
        ordersView.add(order);

        // register in reactor
        reactor.on($(order.getId()), order);

        // register new order as market data listener
        Predicate<IOrderBook> condition =
                new PriceIsWorseOrEqualThanCondition(order.getStopPrice(), order.getDirection());
        marketDataProvider.addListener(order, order.getMarket(), order.getSymbol(), condition);
    }

    public int getLiveOrdersCount() {
        int result = 0;

        for (Order order : orders.values()) {
            if (order.isAlive()) {
                result++;
            }
        }
        return result;
    }

    public void cancel(String internalOrderId) {
        Order order = orders.get(internalOrderId);
        if (order != null) {
            if (order.getStatusReference().compareAndSet(OrderStatus.WAITING, OrderStatus.CANCELLED)) {
                order.updateDisplayStatusAndAction();
                marketDataProvider.removeListener(order, order.getMarket(), order.getSymbol());
            }
        }
    }

    public void remove(String internalOrderId) {
        Order order = orders.get(internalOrderId);
        if (order != null) {
            if (order.getStatus().isTerminal()) {
                orders.remove(internalOrderId);
                ordersView.remove(order);
            }
        }
    }

    public ObservableList<Order> getOrdersView() {
        return ordersView;
    }

    public Collection<Order> getOrders() {
        return orders.values();
    }
}
