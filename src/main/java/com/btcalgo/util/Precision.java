package com.btcalgo.util;

public final class Precision {

    private static final double EPSILON = 0.000000001;

    private Precision() {
    }

    public static boolean isGreaterOrEqual(double a, double b) {
        return areEqual(a, b) || isGreater(a, b);
    }

    public static boolean isGreater(double a, double b) {
        return areNotEqual(a, b) && ((a - b) > EPSILON);
    }

    public static boolean areEqual(double a, double b) {
        return Math.abs(a - b) < EPSILON;
    }

    public static boolean areNotEqual(double a, double b) {
        return !areEqual(a, b);
    }

    public static boolean isLess(double a, double b) {
        return !isGreaterOrEqual(a, b);
    }

}
