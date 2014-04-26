package com.btcalgo.util;

import java.text.DecimalFormat;

public class DoubleFormatter {
    private static ThreadLocal<DecimalFormat> formatter = new ThreadLocal<DecimalFormat>() {
        @Override protected DecimalFormat initialValue() {
            return new DecimalFormat("#.#######################");
        }
    };

    public static String fmt(double number) {
        return formatter.get().format(number);
    }

    public static String fmt(Double number) {
        return (number == null || Double.isNaN(number)) ? "" : fmt(number.doubleValue());
    }
}
