package com.btcalgo.service.api.templates;

import java.util.HashMap;
import java.util.Map;

public class ActiveOrdersTemplate extends LoginTemplate {

    private Map<Long, ActiveOrder> orders = new HashMap<>();

    public Map<Long, ActiveOrder> getOrders() {
        return orders;
    }

    public void setOrders(Map<Long, ActiveOrder> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActiveOrdersTemplate{");
        sb.append("super=").append(super.toString());
        sb.append("orders=").append(orders);
        sb.append('}');
        return sb.toString();
    }
}
