package com.btcalgo.model;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
    NONE("-Select-"),
    BID("Buy"),
    ASK("Sell");

    private String displayName;
    private static List<String> displayNames = new ArrayList<>();

    static {
        for (Direction type : Direction.values()) {
            displayNames.add(type.displayName);
        }
    }

    private Direction(String displayName) {
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

    public static List<String> getDisplayNames() {
        return displayNames;
    }


}
