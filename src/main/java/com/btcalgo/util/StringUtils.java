package com.btcalgo.util;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isNumber(String value) {
        try {
            Double.valueOf(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
