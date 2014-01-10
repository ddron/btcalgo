package com.btcalgo.service.api.templates;

import com.google.gson.annotations.SerializedName;

public class NewOrderTemplate  extends LoginTemplate {
    @SerializedName(value = "return")
    private NewOrder newOrder;

    public NewOrder getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(NewOrder newOrder) {
        this.newOrder = newOrder;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NewOrderTemplate{");
        sb.append("super=").append(super.toString());
        sb.append("newOrder=").append(newOrder);
        sb.append('}');
        return sb.toString();
    }
}
