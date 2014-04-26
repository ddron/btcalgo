package com.btcalgo.service.api;

import com.btcalgo.finances.Finances;
import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.ActiveOrdersTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

import java.util.Map;

import com.btcalgo.service.api.templates.ActiveOrder;

public class ActiveOrdersService implements NameableConsumer<Event<Void>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private IApiService apiService;
    private Finances finances;

    public ActiveOrdersService(IApiService apiService, Finances finances) {
        this.apiService = apiService;
        this.finances = finances;
    }

    @Override
    public String getId() {
        return "activeOrders";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        if (apiService.hasValidKeys()) {
            ActiveOrdersTemplate activeOrders = apiService.getActiveOrders();

            if (activeOrders.isSuccess()) {
                Map<Long, ActiveOrder> orders = activeOrders.getOrders();
                log.debug("Active orders received: {}", orders);
                finances.updateOnOrdersFunds(orders.values());
            } else if ("no orders".equals(activeOrders.getError())){
                finances.clearOnOrdersFunds();
            } else {
                log.error("Error getting active orders: {}", activeOrders.getError());
            }
        }
    }

}
