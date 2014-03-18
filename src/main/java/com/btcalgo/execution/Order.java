package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.marketdata.IMarketDataListener;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;
import com.google.common.util.concurrent.AtomicDouble;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

import java.util.concurrent.atomic.AtomicReference;

import static com.btcalgo.util.DoubleFormatter.fmt;
import static com.btcalgo.util.Precision.EPSILON;
import static com.btcalgo.util.Precision.roundValueToStep;

public abstract class Order implements IMarketDataListener {

    private final String internalOrderId = OrderIdGenerator.nextId();
    protected Logger log = LoggerFactory.getLogger(getClass() + "_" + internalOrderId);

    private final Direction direction;
    private final SymbolEnum symbol;
    private final String market;
    private final StrategyType strategyType;

    private final double amount;

    private OrdersManager ordersManager;

    private final AtomicReference<OrderStatus> status = new AtomicReference<>(OrderStatus.WAITING);
    private ObjectProperty<OrderStatus> displayStatus = new SimpleObjectProperty<>(status.get());
    private StringProperty validAction = new SimpleStringProperty(status.get().getValidAction().name());

    private AtomicDouble stopPriceAsDouble = new AtomicDouble();
    private AtomicDouble limitPriceAsDouble = new AtomicDouble();

    private StringProperty stopPrice = new SimpleStringProperty();
    private StringProperty limitPrice = new SimpleStringProperty();

    private StringProperty initialStopPrice = new SimpleStringProperty(null);
    private StringProperty initialLimitPrice = new SimpleStringProperty(null);

    @Override
    public String getId() {
        return internalOrderId;
    }

    public abstract Predicate<IOrderBook> getObCondition();

    public abstract boolean checkRule(IOrderBook book);

    @Override
    public void accept(Event<IOrderBook> iOrderBookEvent) {
        IOrderBook book = iOrderBookEvent.getData();

        if (book != null && checkRule(book)) {
            if (status.compareAndSet(OrderStatus.WAITING, OrderStatus.SENDING)) {
                log.info("Order {} was triggered by {} update", this, book);
                ordersManager.sendToMarket(this);
            }
        }
    }

    public boolean isAlive() {
        return status.get() == OrderStatus.WAITING;
    }

    public void updateDisplayStatusAndAction() {
        displayStatus.set(status.get());
        validAction.set(displayStatus.get().getValidAction().name());
    }

    public Order(OrdersManager ordersManager, OrderDataHolder holder) {
        this.ordersManager = ordersManager;
        this.direction = holder.getDirection();
        this.symbol = holder.getSymbol();
        this.market = holder.getMarket();
        this.strategyType = holder.getStrategyType();
        this.amount = holder.getAmount();
        setStopPriceAsDouble(holder.getStopPrice());
        setLimitPriceAsDouble(holder.getLimitPrice());
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

    public String getDirectionAsString() {
        return direction.getDisplayName();
    }

    public double getAmount() {
        return amount;
    }

    public String getStopPrice() {
        return stopPrice.get();
    }

    public StringProperty stopPriceProperty() {
        return stopPrice;
    }

    public String getLimitPrice() {
        return limitPrice.get();
    }

    public StringProperty limitPriceProperty() {
        return limitPrice;
    }

    public String getSymbolAsString() {
        return symbol.getDisplayName();
    }

    public String getStrategyTypeAsString() {
        return strategyType.getDisplayName();
    }

    public OrderStatus getDisplayStatus() {
        return displayStatus.get();
    }

    public ObjectProperty<OrderStatus> displayStatusProperty() {
        return displayStatus;
    }

    public void setDisplayStatus(OrderStatus displayStatus) {
        this.displayStatus.set(displayStatus);
    }

    public String getValidAction() {
        return validAction.get();
    }

    public StringProperty validActionProperty() {
        return validAction;
    }

    public void setValidAction(String validAction) {
        this.validAction.set(validAction);
    }

    public String getInitialStopPrice() {
        return initialStopPrice.get();
    }

    public StringProperty initialStopPriceProperty() {
        return initialStopPrice;
    }

    public void setInitialStopPrice(String initialStopPrice) {
        this.initialStopPrice.set(initialStopPrice);
    }

    public String getInitialLimitPrice() {
        return initialLimitPrice.get();
    }

    public StringProperty initialLimitPriceProperty() {
        return initialLimitPrice;
    }

    public void setInitialLimitPrice(String initialLimitPrice) {
        this.initialLimitPrice.set(initialLimitPrice);
    }

    public double getStopPriceAsDouble() {
        return stopPriceAsDouble.get();
    }

    public void setStopPriceAsDouble(double newStopPriceAsDouble) {
        stopPriceAsDouble.set(roundValueToStep(newStopPriceAsDouble, EPSILON));
        stopPriceProperty().set(fmt(getStopPriceAsDouble()));
/*
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            }
        });
*/
    }

    public double getLimitPriceAsDouble() {
        return limitPriceAsDouble.get();
    }

    public void setLimitPriceAsDouble(double newLimitPriceAsDouble) {
        limitPriceAsDouble.set(roundValueToStep(newLimitPriceAsDouble, EPSILON));
        limitPriceProperty().set(fmt(getLimitPriceAsDouble()));
        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
            }
        });*/
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id='").append(internalOrderId).append('\'');
        sb.append(", side=").append(direction);
        sb.append(", symbol=").append(symbol);
        sb.append(", mkt='").append(market).append('\'');
        sb.append(", orderType=").append(strategyType);
        sb.append(", status=").append(status);
        sb.append(", amount=").append(amount);
        sb.append(", stopPrice=").append(stopPrice.get());
        sb.append(", limitPrice=").append(limitPrice.get());
        return sb.toString();
    }
}
