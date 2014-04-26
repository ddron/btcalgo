package com.btcalgo.finances;

import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.templates.FeeTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FeeInfo {

    private Logger log = LoggerFactory.getLogger(getClass());

    private Map<SymbolEnum, Double> fees = new ConcurrentHashMap<>();

    public void update(FeeTemplate feeTemplate) {
        fees.put(feeTemplate.getSymbol(), feeTemplate.getTrade());

        log.debug("Fees: {}", fees);
    }

    public double getFee(SymbolEnum symbolEnum) {
        Double result = fees.get(symbolEnum);
        return result == null ? 0 : result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FeeInfo{");
        sb.append("fees=").append(fees);
        sb.append('}');
        return sb.toString();
    }
}
