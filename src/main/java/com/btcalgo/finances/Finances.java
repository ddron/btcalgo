package com.btcalgo.finances;

import com.btcalgo.service.api.templates.ActiveOrder;
import com.btcalgo.service.api.templates.Funds;
import com.btcalgo.ui.model.FinancesToShow;
import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.btcalgo.finances.FundsEnum.*;

public class Finances {

    private Map<FundsEnum, Double> availableFunds = ImmutableMap.of();
    private Map<FundsEnum, Double> onOrdersFunds = ImmutableMap.of();

    private FinancesToShow financesToShow;

    public void updateAvailableFunds(Funds funds) {
        ImmutableMap.Builder<FundsEnum, Double> builder = new ImmutableMap.Builder<>();

        builder.put(BTC, funds.getBtc());
        builder.put(LTC, funds.getLtc());
        builder.put(NMC, funds.getNmc());
        builder.put(NVC, funds.getNvc());
        builder.put(TRC, funds.getTrc());
        builder.put(PPC, funds.getPpc());
        builder.put(FTC, funds.getFtc());
        builder.put(XPM, funds.getXpm());
        builder.put(USD, funds.getUsd());
        builder.put(RUR, funds.getRur());
        builder.put(EUR, funds.getEur());
        builder.put(CNH, funds.getCnh());
        builder.put(GBP, funds.getGbp());

        this.availableFunds = builder.build();

        financesToShow.updateFinancesToShow();
    }

    public void updateOnOrdersFunds(Collection<ActiveOrder> activeOrders) {
        Map<FundsEnum, Double> result = new HashMap<>();
        for (ActiveOrder activeOrder : activeOrders) {
            FundsEnum coverFund = activeOrder.getCoverFund();
            Double coverAmount = activeOrder.getCoverFundAmount();

            Double existentAmount = result.get(coverFund) == null ? 0 : result.get(coverFund);
            result.put(coverFund, existentAmount + coverAmount);
        }
        this.onOrdersFunds = ImmutableMap.copyOf(result);
    }

    public void clearOnOrdersFunds() {
        this.onOrdersFunds = ImmutableMap.of();
    }

    public Map<FundsEnum, Double> getAvailableFunds() {
        return availableFunds;
    }

    public Map<FundsEnum, Double> getOnOrdersFunds() {
        return onOrdersFunds;
    }

    public void setFinancesToShow(FinancesToShow financesToShow) {
        this.financesToShow = financesToShow;
    }
}
