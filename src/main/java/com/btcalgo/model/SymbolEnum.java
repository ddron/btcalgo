package com.btcalgo.model;

import java.util.ArrayList;
import java.util.List;

public enum SymbolEnum {
    BTCUSD("btc_usd", "BTC/USD", 0.01, 0.00000001),
    BTCRUR("btc_rur", "BTC/RUR", 0.01, 0.00000001),
    BTCEUR("btc_eur", "BTC/EUR", 0.01, 0.00000001),

    LTCBTC("ltc_btc", "LTC/BTC", 0.1, 0.00000001),
    LTCUSD("ltc_usd", "LTC/USD", 0.1, 0.00000001),
    LTCRUR("ltc_rur", "LTC/RUR", 0.1, 0.00000001),
    LTCEUR("ltc_eur", "LTC/EUR", 0.1, 0.00000001),

    NMCBTC("nmc_btc", "NMC/BTC", 0.1, 0.00000001),
    NMCUSD("nmc_usd", "NMC/USD", 0.1, 0.00000001),

    NVCBTC("nvc_btc", "NVC/BTC", 0.1, 0.00000001),
    NVCUSD("nvc_usd", "NVC/USD", 0.1, 0.00000001),

    USDRUR("usd_rur", "USD/RUR", 0.1, 0.00000001),

    EURUSD("eur_usd", "EUR/USD", 0.1, 0.00000001),
    EURRUR("eur_rur", "EUR/RUR", 0.1, 0.00000001),

    TRCBTC("trc_btc", "TRC/BTC", 0.1, 0.00000001),

    PPCBTC("ppc_btc", "PPC/BTC", 0.1, 0.00000001),
    PPCUSD("ppc_usd", "PPC/USD", 0.1, 0.00000001),

    FTCBTC("ftc_btc", "FTC/BTC", 0.1, 0.00000001),

    XPMBTC("xpm_btc", "XPM/BTC", 0.1, 0.00000001);

    private String value;
    private String displayName;
    private double minSize;
    private double minStep;

    private static List<String> displayNames = new ArrayList<>();

    static {
        for (SymbolEnum type : SymbolEnum.values()) {
            displayNames.add(type.displayName);
        }
    }

    private SymbolEnum(String value, String displayName, double minSize, double minStep) {
        this.value = value;
        this.displayName = displayName;
        this.minSize = minSize;
        this.minStep = minStep;
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

    public double getMinSize() {
        return minSize;
    }

    public double getMinStep() {
        return minStep;
    }

    public String getFirst() {
        return getDisplayName().substring(0, 3);
    }
}
