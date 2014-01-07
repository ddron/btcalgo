package com.btcalgo.service.api;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.ui.model.KeysStatusHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class InfoService implements NameableConsumer<Event<Void>> {

    private static final String API_METHOD_NAME = "getInfo";

    private Logger log = LoggerFactory.getLogger(getClass());

    private ApiService apiService;
    private KeysStatusHolder keysStatusHolder;

    public InfoService(ApiService apiService, KeysStatusHolder keysStatusHolder) {
        this.apiService = apiService;
        this.keysStatusHolder = keysStatusHolder;
    }

    @Override
    public String getId() {
        return "info";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        InfoTemplate infoTemplate = apiService.auth(API_METHOD_NAME, InfoTemplate.class);
        keysStatusHolder.updateStatus(infoTemplate);

        log.debug("{}", infoTemplate);
    }
}
