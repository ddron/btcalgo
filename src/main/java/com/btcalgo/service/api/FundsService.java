package com.btcalgo.service.api;

import com.btcalgo.model.Finances;
import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.Funds;
import com.btcalgo.service.api.templates.InfoTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class FundsService implements NameableConsumer<Event<Void>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private IApiService apiService;
    private Finances finances;

    public FundsService(IApiService apiService, Finances finances) {
        this.apiService = apiService;
        this.finances = finances;
    }

    @Override
    public String getId() {
        return "funds";
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        if (apiService.hasValidKeys()) {
            InfoTemplate infoTemplate = apiService.getInfo();
            Funds funds = infoTemplate.getInfo().getFunds();

            log.debug("Funds received: {}", funds);

            finances.updateAvailableFunds(funds);
        }

    }
}
