package com.btcalgo.service.marketdata;

public enum SymbolEnum {
    BTCUSD("btc_usd"),
    BTCRUR("btc_rur"),
    BTCEUR("btc_eur"),
    LTCBTC("ltc_btc"),
    LTCUSD("ltc_usd"),
    LTCRUR("ltc_rur"),
    LTCEUR("ltc_eur"),
    NMCBTC("nmc_btc"),
    NMCUSD("nmc_usd"),
    NVCBTC("nvc_btc"),
    NVCUSD("nvc_usd"),
    USDRUR("usd_rur"),
    EURUSD("eur_usd"),
    TRCBTC("trc_btc"),
    PPCBTC("ppc_btc"),
    PPCUSD("ppc_usd"),
    FTCBTC("ftc_btc"),
    XPMBTC("xpm_btc");

    private String value;

    private SymbolEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
