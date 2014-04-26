package com.btcalgo.service.api;

import com.btcalgo.finances.FeeInfo;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.service.api.templates.FeeTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class FeeService implements NameableConsumer<Event<SymbolEnum>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    private IApiService apiService;
    private FeeInfo feeInfo;

    public FeeService(IApiService apiService, FeeInfo feeInfo) {
        this.apiService = apiService;
        this.feeInfo = feeInfo;
    }

    @Override
    public String getId() {
        return "FeeService";
    }

    @Override
    public void accept(Event<SymbolEnum> symbolEnumEvent) {
        SymbolEnum symbol = symbolEnumEvent.getData();
        FeeTemplate feeTemplate = apiService.getFee(symbol.getValue());

        if (feeTemplate != null) {
            feeTemplate.setSymbol(symbol);
            log.debug("Fee for {} is {}", symbol, feeTemplate.getTrade());

            feeInfo.update(feeTemplate);
        }

    }
}
