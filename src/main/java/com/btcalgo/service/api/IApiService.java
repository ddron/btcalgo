package com.btcalgo.service.api;

import com.btcalgo.execution.Order;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.service.api.templates.NewOrderTemplate;
import com.btcalgo.service.api.templates.TickerTemplate;

public interface IApiService {

    void updateKeys(String key, String secret);

    boolean hasValidKeys();

    void setValidKeys(boolean validKeys);


    TickerTemplate getTicker(String symbol);

    NewOrderTemplate sendNewOrder(Order order);

    InfoTemplate getInfo();
}
