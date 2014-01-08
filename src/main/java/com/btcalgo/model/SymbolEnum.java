package com.btcalgo.model;

import java.util.ArrayList;
import java.util.List;

public enum SymbolEnum {
    BTCUSD("btc_usd", "BTC/USD"),
    BTCRUR("btc_rur", "BTC/RUR"),
    BTCEUR("btc_eur", "BTC/EUR"),
    LTCBTC("ltc_btc", "LTC/BTC"),
    LTCUSD("ltc_usd", "LTC/USD"),
    LTCRUR("ltc_rur", "LTC/RUR"),
    LTCEUR("ltc_eur", "LTC/EUR"),
    NMCBTC("nmc_btc", "NMC/BTC"),
    NMCUSD("nmc_usd", "NMC/USD"),
    NVCBTC("nvc_btc", "NVC/BTC"),
    NVCUSD("nvc_usd", "NVC/USD"),
    USDRUR("usd_rur", "USD/RUR"),
    EURUSD("eur_usd", "EUR/USD"),
    TRCBTC("trc_btc", "TRC/BTC"),
    PPCBTC("ppc_btc", "PPC/BTC"),
    PPCUSD("ppc_usd", "PPC/USD"),
    FTCBTC("ftc_btc", "FTC/BTC"),
    XPMBTC("xpm_btc", "XPM/BTC");

    private String value;
    private String displayName;

    private static List<String> displayNames = new ArrayList<>();

    static {
        for (SymbolEnum type : SymbolEnum.values()) {
            displayNames.add(type.displayName);
        }
    }

    private SymbolEnum(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SymbolEnum valueByDisplayName(String displayName) {
        for (SymbolEnum type : SymbolEnum.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return null;
    }

    public static List<String> getDisplayNames() {
        return displayNames;
    }
}
