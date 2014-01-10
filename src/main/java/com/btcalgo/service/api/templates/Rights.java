package com.btcalgo.service.api.templates;

public class Rights {
    private int info;
    private int trade;

    public int getInfo() {
        return info;
    }

    public void setInfo(int info) {
        this.info = info;
    }

    public int getTrade() {
        return trade;
    }

    public void setTrade(int trade) {
        this.trade = trade;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rights{");
        sb.append("info=").append(info);
        sb.append(", trade=").append(trade);
        sb.append('}');
        return sb.toString();
    }
}
