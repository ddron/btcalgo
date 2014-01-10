package com.btcalgo.service.api.templates;

public class NewOrder {
    private double received;
    private double remains;
    private long orderId;
    private Funds funds;

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public double getRemains() {
        return remains;
    }

    public void setRemains(double remains) {
        this.remains = remains;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Funds getFunds() {
        return funds;
    }

    public void setFunds(Funds funds) {
        this.funds = funds;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NewOrder{");
        sb.append("received=").append(received);
        sb.append(", remains=").append(remains);
        sb.append(", orderId=").append(orderId);
        sb.append(", funds=").append(funds);
        sb.append('}');
        return sb.toString();
    }
}
