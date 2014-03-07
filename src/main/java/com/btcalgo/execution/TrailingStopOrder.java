package com.btcalgo.execution;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.ui.model.OrderDataHolder;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class TrailingStopOrder extends Order {

    public TrailingStopOrder(OrdersManager ordersManager, OrderDataHolder holder) {
        super(ordersManager, holder);

        setInitialStopPrice(getStopPrice());
        setInitialLimitPrice(getLimitPrice());

        log.info("New order created: {}", this);
    }

    @Override
    public Predicate<IOrderBook> getObCondition() {
        return Predicates.alwaysTrue();
    }

    @Override
    public boolean checkRule(IOrderBook book) {
        // TODO: implement
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
