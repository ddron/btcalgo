package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.ApiService;
import com.btcalgo.service.marketdata.IMarketDataListener;
import com.btcalgo.service.marketdata.MarketDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

import java.util.concurrent.atomic.AtomicReference;

public class Order implements IMarketDataListener {

    private final String internalOrderId = OrderIdGenerator.nextId();
    private Logger log = LoggerFactory.getLogger(getClass() + internalOrderId);

    private final Direction direction;
    private final SymbolEnum symbol;
    private final String market;
    private final StrategyType strategyType;

    private final double amount;
    private final double stopPrice;
    private final double limitPrice;

    private ApiService apiService;
    private MarketDataProvider marketDataProvider;

    private final AtomicReference<OrderStatus> status = new AtomicReference<>(OrderStatus.WAITING);

    @Override
    public String getId() {
        return internalOrderId;
    }

    @Override
    public void accept(Event<IOrderBook> iOrderBookEvent) {
        IOrderBook book = iOrderBookEvent.getData();
        if (book != null && status.compareAndSet(OrderStatus.WAITING, OrderStatus.SENDING)) {
            log.info("Order {} was triggered by {} update", this, book);
            marketDataProvider.removeListener(this, market, symbol);

            // TODO: remove next line. And uncomment code block below
            status.set(OrderStatus.SENT);
            /*NewOrderTemplate newOrderTemplate = apiService.sendNewOrder(symbol, direction, limitPrice, amount);
            if (newOrderTemplate.isSuccess()) {
                status.set(OrderStatus.SENT);
            } else {
                status.set(OrderStatus.ERROR);
            }*/
        }
    }

    public static class OrderBuilder {
        private ApiService apiService;
        private MarketDataProvider marketDataProvider;

        private Direction direction;
        private SymbolEnum symbol;
        private String market;
        private StrategyType strategyType;

        private double amount;
        private double stopPrice;
        private double limitPrice;

        /** use {@link com.btcalgo.execution.Order.OrderBuilder#newOrder()} */
        private OrderBuilder() {
        }

        public static OrderBuilder newOrder() {
            return new OrderBuilder();
        }

        public OrderBuilder setApiService(ApiService apiService) {
            this.apiService = apiService;
            return this;
        }

        public OrderBuilder setMarketDataProvider(MarketDataProvider marketDataProvider) {
            this.marketDataProvider = marketDataProvider;
            return this;
        }

        public OrderBuilder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public OrderBuilder setSymbol(SymbolEnum symbol) {
            this.symbol = symbol;
            return this;
        }

        public OrderBuilder setMarket(String market) {
            this.market = market;
            return this;
        }

        public OrderBuilder setStrategyType(StrategyType strategyType) {
            this.strategyType = strategyType;
            return this;
        }

        public OrderBuilder setAmount(double amount) {
            this.amount = amount;
            return this;
        }

        public OrderBuilder setStopPrice(double stopPrice) {
            this.stopPrice = stopPrice;
            return this;
        }

        public OrderBuilder setLimitPrice(double limitPrice) {
            this.limitPrice = limitPrice;
            return this;
        }

        public Order build() {
            return new Order(apiService, marketDataProvider, direction, symbol, market, strategyType,
                    amount, stopPrice, limitPrice);
        }
    }

    private Order(ApiService apiService, MarketDataProvider marketDataProvider, Direction direction, SymbolEnum symbol,
                  String market,
                  StrategyType strategyType, double amount,
                 double stopPrice, double limitPrice) {
        this.apiService = apiService;
        this.marketDataProvider = marketDataProvider;
        this.direction = direction;
        this.symbol = symbol;
        this.market = market;
        this.strategyType = strategyType;
        this.amount = amount;
        this.stopPrice = stopPrice;
        this.limitPrice = limitPrice;

        log.info("New order created: {}", this);
    }

    public Direction getDirection() {
        return direction;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public String getMarket() {
        return market;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public double getAmount() {
        return amount;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public OrderStatus getStatus() {
        return status.get();
    }

    public AtomicReference<OrderStatus> getStatusReference() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status.set(status);
    }

    public String getInternalOrderId() {
        return internalOrderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("internalOrderId='").append(internalOrderId).append('\'');
        sb.append(", direction=").append(direction);
        sb.append(", symbol=").append(symbol);
        sb.append(", market='").append(market).append('\'');
        sb.append(", strategyType=").append(strategyType);
        sb.append(", amount=").append(amount);
        sb.append(", stopPrice=").append(stopPrice);
        sb.append(", limitPrice=").append(limitPrice);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
