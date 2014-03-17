package com.btcalgo.ui.model;

import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;

public class OrderDataHolder {
    private final Direction direction;
    private final SymbolEnum symbol;
    @SuppressWarnings("FieldCanBeLocal")
    private final String market = "BTCE";
    private final StrategyType strategyType;

    private final double amount;
    private final double stopPrice;
    private final double limitPrice;

    /* This for trailing stop loss algo */
    private final double offset;

    public static class OrderDataHolderBuilder {
        private Direction direction;
        private SymbolEnum symbol;
        private StrategyType strategyType;

        private double amount;
        private double stopPrice;
        private double limitPrice;

        private double offset;

        private OrderDataHolderBuilder() {
        }

        public static OrderDataHolderBuilder newOrderDataHolder() {
            return new OrderDataHolderBuilder();
        }

        public OrderDataHolderBuilder setDirection(String displayName) {
            this.direction = Direction.valueByDisplayName(displayName);
            return this;
        }

        public OrderDataHolderBuilder setSymbol(String displayName) {
            this.symbol = SymbolEnum.valueByDisplayName(displayName);
            return this;
        }

        public OrderDataHolderBuilder setStrategyType(String displayName) {
            this.strategyType = StrategyType.valueByDisplayName(displayName);
            return this;
        }

        public OrderDataHolderBuilder setAmount(String amountAsString) {
            this.amount = Double.valueOf(amountAsString);
            return this;
        }

        public OrderDataHolderBuilder setStopPrice(String stopPriceAsString) {
            this.stopPrice = Double.valueOf(stopPriceAsString);
            return this;
        }

        public OrderDataHolderBuilder setLimitPrice(String limitPriceAsString) {
            this.limitPrice = Double.valueOf(limitPriceAsString);
            return this;
        }

        public OrderDataHolderBuilder setOffset(double offset) {
            this.offset = offset;
            return this;
        }

        public OrderDataHolder build() {
            return new OrderDataHolder(direction, symbol, strategyType, amount, stopPrice, limitPrice, offset);
        }
    }

    private OrderDataHolder(Direction direction, SymbolEnum symbol, StrategyType strategyType,
                           double amount, double stopPrice, double limitPrice, double offset) {
        this.direction = direction;
        this.symbol = symbol;
        this.strategyType = strategyType;
        this.amount = amount;
        this.stopPrice = stopPrice;
        this.limitPrice = limitPrice;
        this.offset = offset;
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

    public double getOffset() {
        return offset;
    }
}
