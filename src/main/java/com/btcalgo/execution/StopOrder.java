package com.btcalgo.execution;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.service.marketdata.PriceIsWorseOrEqualThanCondition;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;

public class StopOrder extends Order {

    private Predicate<IOrderBook> obCondition;

    public StopOrder(OrdersManager ordersManager, OrderDataHolder holder) {
        super(ordersManager, holder);

        obCondition = new PriceIsWorseOrEqualThanCondition(getStopPriceAsDouble(), getDirection());
        log.info("New order created: {}", this);
    }

    @Override
    public Predicate<IOrderBook> getObCondition() {
        return obCondition;
    }

    @Override
    public boolean checkRule(IOrderBook book) {
        return obCondition != null && obCondition.apply(book);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
