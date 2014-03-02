package com.btcalgo.service.api;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.ui.model.KeysStatusHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class InfoService implements NameableConsumer<Event<Void>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private IApiService apiService;
    private KeysStatusHolder keysStatusHolder;

    public InfoService(IApiService apiService, KeysStatusHolder keysStatusHolder) {
        this.apiService = apiService;
        this.keysStatusHolder = keysStatusHolder;
    }

    @Override
    public String getId() {
        return "info";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        InfoTemplate infoTemplate = apiService.getInfo();
        apiService.setValidKeys(infoTemplate.hasAllRights());

        keysStatusHolder.updateStatus(infoTemplate);

        log.debug("{}", infoTemplate);
    }
}
