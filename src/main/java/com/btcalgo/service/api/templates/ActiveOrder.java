package com.btcalgo.service.api.templates;

import com.btcalgo.model.Direction;
import com.btcalgo.model.FundsEnum;
import com.btcalgo.model.SymbolEnum;

public class ActiveOrder {
    private SymbolEnum symbol;
    private Direction direction; // buy / sell
    private double amount;
    private double rate; //price
    private long timestamp_created;
    private int status;

    /**
     * Active order is intention. We do not yet have this BTC we're about to buy.
     * We still have USD in fact. But how much? This we call cover funds. And this method
     * calculates it and returns.
     */
    public double getCoverFundAmount() {
        return direction == Direction.BID ? amount * rate : amount;
    }

    public FundsEnum getCoverFund() {
        return FundsEnum.valueOf(direction == Direction.BID ? symbol.getSecond() : symbol.getFirst());
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public void setSymbol(SymbolEnum symbol) {
        this.symbol = symbol;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public long getTimestamp_created() {
        return timestamp_created;
    }

    public void setTimestamp_created(long timestamp_created) {
        this.timestamp_created = timestamp_created;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("symbol='").append(symbol).append('\'');
        sb.append(", direction='").append(direction).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", rate=").append(rate);
        sb.append(", timestamp_created=").append(timestamp_created);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
