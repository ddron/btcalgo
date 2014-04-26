package com.btcalgo.service.api.templates;

import com.btcalgo.model.SymbolEnum;

public class FeeTemplate {

    private double trade;
    private SymbolEnum symbol;

    public double getTrade() {
        return trade;
    }

    public void setTrade(double trade) {
        this.trade = trade;
    }

    public SymbolEnum getSymbol() {
        return symbol;
    }

    public void setSymbol(SymbolEnum symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FeeTemplate{");
        sb.append("trade=").append(trade);
        sb.append(", symbol=").append(symbol);
        sb.append('}');
        return sb.toString();
    }
}
