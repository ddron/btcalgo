package com.btcalgo.execution;

import com.btcalgo.service.api.IApiService;
import com.btcalgo.service.api.templates.NewOrderTemplate;
import com.btcalgo.service.marketdata.IMarketDataProvider;
import com.btcalgo.ui.model.OrderDataHolder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Reactor;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static reactor.event.selector.Selectors.$;

public class OrdersManager {

    private Logger log = LoggerFactory.getLogger(getClass());

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
        Order order;
        if (holder.getStrategyType() == StrategyType.STOP_LOSS) {
            order = new StopOrder(this, holder);
        } else if (holder.getStrategyType() == StrategyType.TRAILING_STOP) {
            order = new TrailingStopOrder(this, holder);
        } else {
            log.error("Unknown strategy type: {}", holder.getStrategyType());
            return;
        }

        // store reference to internal map
        orders.put(order.getInternalOrderId(), order);
        ordersView.add(order);

        // register in reactor
        reactor.on($(order.getId()), order);

        // register new order as market data listener
        marketDataProvider.addListener(order, order.getMarket(), order.getSymbol(), order.getObCondition());
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

    public void sendToMarket(Order order) {
        order.updateDisplayStatusAndAction();
        marketDataProvider.removeListener(order, order.getMarket(), order.getSymbol());

        NewOrderTemplate newOrderTemplate = apiService.sendNewOrder(order);
        if (newOrderTemplate.isSuccess()) {
            log.info("order {} was successfully sent to market.", order.getInternalOrderId());
            order.setStatus(OrderStatus.SENT);
        } else {
            log.error("order {} was NOT sent to market. Result: {}", order.getInternalOrderId(), newOrderTemplate.getError());
            order.setStatus(OrderStatus.ERROR);
        }
        order.updateDisplayStatusAndAction();
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
