package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import com.btcalgo.model.IOrderBook;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import static com.btcalgo.util.Precision.EPSILON;
import static com.btcalgo.util.Precision.roundValueToStep;

public class TrailingStopOrder extends Order {

    private final double offset; // offset between stop price and current market price
    private final double limitPriceOffset; // offset between stop price and limit price

    public TrailingStopOrder(OrdersManager ordersManager, OrderDataHolder holder) {
        super(ordersManager, holder);

        setInitialStopPrice(getStopPrice());
        setInitialLimitPrice(getLimitPrice());

        offset = holder.getOffset();
        limitPriceOffset = roundValueToStep(getLimitPriceAsDouble() - getStopPriceAsDouble(), EPSILON);

        log.info("New order created: {}", this);
    }

    @Override
    public Predicate<IOrderBook> getObCondition() {
        return Predicates.alwaysTrue();
    }

    @Override
    public boolean checkRule(IOrderBook book) {
        if (book == null) {
            return false;
        }

        double obPrice = getDirection() == Direction.BID ? book.getBestBidPrice() : book.getBestAskPrice();
        if (!Double.isNaN(obPrice)) {
            double currentStopPrice = getStopPriceAsDouble();

            int result = getDirection() == Direction.BID
                    ? Double.compare(obPrice, currentStopPrice)
                    : Double.compare(currentStopPrice, obPrice);
            if (result >= 0) {
                return true;
            }

            if (Math.abs(obPrice - currentStopPrice) > offset) {
                if (getDirection() == Direction.BID) {
                    setStopPriceAsDouble(obPrice + offset);
                } else {
                    setStopPriceAsDouble(obPrice - offset);
                }
                setLimitPriceAsDouble(getStopPriceAsDouble() + limitPriceOffset);

                log.info("Best market price({}): {}. New stopPrice={}, new limitPrice={}",
                        getDirection(), obPrice, getStopPrice(), getLimitPrice());
            }
        }

        return false;
    }

    public double getOffset() {
        return offset;
    }

    public double getLimitPriceOffset() {
        return limitPriceOffset;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append(", offset=").append(offset);
        sb.append('}');
        return sb.toString();
    }
}
