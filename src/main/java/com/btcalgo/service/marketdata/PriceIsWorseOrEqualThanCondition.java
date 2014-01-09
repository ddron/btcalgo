package com.btcalgo.service.marketdata;

import com.btcalgo.model.Direction;
import com.btcalgo.model.IOrderBook;
import com.google.common.base.Predicate;

/** Meaning that price is worse for US, so need to trigger stop loss*/
public class PriceIsWorseOrEqualThanCondition implements Predicate<IOrderBook> {
    private double price;
    private Direction direction;

    public PriceIsWorseOrEqualThanCondition(double price, Direction direction) {
        this.price = price;
        this.direction = direction;
    }

    @Override
    public boolean apply(IOrderBook input) {
        if (input == null) {
            return false;
        }

        double obPrice = direction == Direction.BID ? input.getBestBidPrice() : input.getBestAskPrice();
        if (!Double.isNaN(obPrice)) {
            int result = direction == Direction.BID ? Double.compare(obPrice, price) : Double.compare(price, obPrice);
            return result >= 0;
        }

        return false;
    }
}
