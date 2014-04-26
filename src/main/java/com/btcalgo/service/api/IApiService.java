package com.btcalgo.service.api;

import com.btcalgo.execution.Order;
import com.btcalgo.service.api.templates.*;

public interface IApiService {

    void updateKeys(String key, String secret);

    boolean hasValidKeys();

    void setValidKeys(boolean validKeys);


    TickerTemplate getTicker(String symbol);

    NewOrderTemplate sendNewOrder(Order order);

    InfoTemplate getInfo();

    ActiveOrdersTemplate getActiveOrders();

    FeeTemplate getFee(String symbol);
}
