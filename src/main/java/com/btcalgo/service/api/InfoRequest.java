package com.btcalgo.service.api;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.InfoTemplate;
import reactor.event.Event;

public class InfoRequest implements NameableConsumer<Event<Void>> {

    private static final String API_METHOD_NAME = "getInfo";

    private ApiService apiService;

    public InfoRequest(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public String getId() {
        return "info";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        apiService.auth(API_METHOD_NAME, InfoTemplate.class);
    }
}
