package com.btcalgo.execution;

import java.util.ArrayList;
import java.util.List;

public enum StrategyType {
    STOP_LOSS("Stop Loss"),
    TRAILING_STOP("Trailing Stop");

    private String displayName;
    private static List<String> displayNames = new ArrayList<>();

    static {
        for (StrategyType type : StrategyType.values()) {
            displayNames.add(type.displayName);
        }
    }

    private StrategyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static StrategyType valueByDisplayName(String displayName) {
        for (StrategyType type : StrategyType.values()) {
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
