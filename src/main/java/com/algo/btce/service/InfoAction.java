package com.algo.btce.service;

import com.algo.btce.reactor.NameableConsumer;
import com.algo.btce.templates.InfoTemplate;
import reactor.event.Event;

public class InfoAction implements NameableConsumer<Event<Void>> {

    private static final String API_METHOD_NAME = "getInfo";

    private AuthService authService;

    public InfoAction(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public String getId() {
        return "info";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        authService.auth(API_METHOD_NAME, InfoTemplate.class);
    }
}
