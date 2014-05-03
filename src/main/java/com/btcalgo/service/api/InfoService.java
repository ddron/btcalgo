package com.btcalgo.service.api;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.InfoTemplate;
import com.btcalgo.ui.KeysController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class InfoService implements NameableConsumer<Event<Void>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private IApiService apiService;
    private KeysController keysController;

    private FundsService fundsService;
    private ActiveOrdersService activeOrdersService;

    public InfoService(IApiService apiService, KeysController keysController) {
        this.apiService = apiService;
        this.keysController = keysController;
    }

    @Override
    public String getId() {
        return "info";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        InfoTemplate infoTemplate = apiService.getInfo();

        apiService.setValidKeys(infoTemplate.hasAllRights());
        keysController.updateStatus(infoTemplate);

        if (apiService.hasValidKeys()) {
            if (fundsService != null) {
                fundsService.accept(null);
            }
            if (activeOrdersService != null) {
                activeOrdersService.accept(null);
            }
        }

        log.debug("{}", infoTemplate);
    }

    public void setFundsService(FundsService fundsService) {
        this.fundsService = fundsService;
    }

    public void setActiveOrdersService(ActiveOrdersService activeOrdersService) {
        this.activeOrdersService = activeOrdersService;
    }
}
