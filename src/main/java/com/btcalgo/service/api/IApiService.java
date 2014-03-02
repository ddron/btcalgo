package com.btcalgo.service.api;

import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.service.api.templates.NewOrderTemplate;
import com.btcalgo.service.api.templates.TickerTemplate;

public interface IApiService {

    void updateKeys(String key, String secret);

    boolean hasValidKeys();

    void setValidKeys(boolean validKeys);


    TickerTemplate getTicker(String symbol);

    NewOrderTemplate sendNewOrder(SymbolEnum symbol, Direction direction, double price, double amount);

    InfoTemplate getInfo();
}
