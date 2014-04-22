package com.btcalgo.model;

import com.btcalgo.service.api.templates.Funds;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.btcalgo.model.FundsEnum.*;

import com.btcalgo.service.api.templates.ActiveOrder;

public class Finances {

    private Map<FundsEnum, Double> availableFunds = new ConcurrentHashMap<>();
    private Map<FundsEnum, Double> onOrdersFunds = new ConcurrentHashMap<>();


    public void updateAvailableFunds(Funds funds) {
        availableFunds.put(BTC, funds.getBtc());
        availableFunds.put(LTC, funds.getLtc());
        availableFunds.put(NMC, funds.getNmc());
        availableFunds.put(NVC, funds.getNvc());
        availableFunds.put(TRC, funds.getTrc());
        availableFunds.put(PPC, funds.getPpc());
        availableFunds.put(FTC, funds.getFtc());
        availableFunds.put(XPM, funds.getXpm());
        availableFunds.put(USD, funds.getUsd());
        availableFunds.put(RUR, funds.getRur());
        availableFunds.put(EUR, funds.getEur());
        availableFunds.put(CNH, funds.getCnh());
        availableFunds.put(GBP, funds.getGbp());
    }

    public void updateOnOrdersFunds(Collection<ActiveOrder> values) {

    }
}
