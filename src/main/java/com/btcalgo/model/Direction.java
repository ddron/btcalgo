package com.btcalgo.model;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
    NONE("", "Buy/Sell?"),
    BID("buy", "Buy"),
    ASK("sell", "Sell");

    private String apiValue;
    private String displayName;
    private static List<String> displayNames = new ArrayList<>();

    static {
        for (Direction type : Direction.values()) {
            displayNames.add(type.displayName);
        }
    }

    private Direction(String apiValue, String displayName) {
        this.apiValue = apiValue;
        this.displayName = displayName;
    }

    public static Direction valueByDisplayName(String displayName) {
        for (Direction type : Direction.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        return null;
    }

    public static Direction valueByApiValue(String apiValue) {
        for (Direction type : Direction.values()) {
            if (type.apiValue.equals(apiValue)) {
                return type;
            }
        }
        return Direction.NONE;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getApiValue() {
        return apiValue;
    }

    public static List<String> getDisplayNames() {
        return displayNames;
    }


}
