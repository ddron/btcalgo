package com.btcalgo.util;

public final class Precision {

    public static final double EPSILON = 0.0000000001;

    private Precision() {
    }

    public static boolean isZero(double a) {
        return areEqual(a, 0);
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

    public static boolean isAlignedAgainst(double value, double delta) {
        return isZero(Math.IEEEremainder(value, delta));
    }

    public static double roundDownValueToStep(double rawValue, double valueStep) {
        valueStep = 1 / valueStep;
        return Math.floor(rawValue * valueStep + EPSILON) / valueStep;
    }

    public static double roundUpValueToStep(double rawValue, double valueStep) {
        valueStep = 1 / valueStep;
        return Math.ceil(rawValue * valueStep - EPSILON) / valueStep;
    }

    public static double roundValueToStep(double rawValue, double valueStep) {
        double remainder = Math.IEEEremainder(rawValue, valueStep);
        if (remainder > 0) {
            return roundDownValueToStep(rawValue, valueStep);
        } else if (remainder < 0) {
            return roundUpValueToStep(rawValue, valueStep);
        } else {
            return rawValue;
        }
    }

}
