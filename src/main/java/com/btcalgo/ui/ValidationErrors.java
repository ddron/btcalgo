package com.btcalgo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.btcalgo.ui.ValidationErrors.ErrorType.*;
import static com.btcalgo.ui.ValidationErrors.Field.*;

public class ValidationErrors {

    public static enum Field {
        KEY,
        SECRET,
        SYMBOL,
        STRATEGY_TYPE,
        DIRECTION,
        AMOUNT,

        STOP_PRICE,
        LIMIT_PRICE,
        OFFSET,

        GENERAL
    }

    public static enum ErrorType {
        EMPTY,
        FORMAT,
        INCORRECT_VALUE
    }

    private static final Map<Field, Map<ErrorType, String>> ERRORS = new HashMap<Field, Map<ErrorType, String>>() {
        {
            put(KEY, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Key is empty");
                }
            });
            put(SECRET, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Secret is empty");
                }
            });
            put(SYMBOL, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Currency is not set");
                    put(INCORRECT_VALUE, "Currency is incorrect");
                }
            });
            put(STRATEGY_TYPE, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Order type is empty");
                    put(INCORRECT_VALUE, "Order Type is incorrect");
                }
            });
            put(DIRECTION, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Side (Buy/Sell) is empty");
                    put(INCORRECT_VALUE, "Side (Buy/Sell) is incorrect");
                }
            });
            put(AMOUNT, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Amount is empty");
                    put(FORMAT, "Amount is not a number");
                    put(INCORRECT_VALUE, "Amount is incorrect");
                }
            });
            put(STOP_PRICE, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Stop price is empty");
                    put(FORMAT, "Stop price is not a number");
                    put(INCORRECT_VALUE, "Stop price is incorrect");
                }
            });
            put(LIMIT_PRICE, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Price is empty");
                    put(FORMAT, "Price is not a number");
                    put(INCORRECT_VALUE, "Price is incorrect");
                }
            });
            put(OFFSET, new HashMap<ErrorType, String>() {
                {
                    put(EMPTY, "Offset is empty");
                    put(FORMAT, "Offset is not a number");
                    put(INCORRECT_VALUE, "Offset is incorrect");
                }
            });
            put(GENERAL, new HashMap<ErrorType, String>() {
                {

                }
            });
        }
    };

    public static String getErrorValue(Field field, ErrorType errorType) {
        Map<ErrorType, String> errorsForField = ERRORS.get(field);
        if (errorsForField != null) {
            return errorsForField.get(errorType);
        }
        return null;
    }

    public static List<String> getErrorValues(Map<Field, List<ErrorType>> errors) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<Field, List<ErrorType>>  entry : errors.entrySet()) {
            Field field = entry.getKey();
            for (ErrorType errorType : entry.getValue()) {
                result.add(getErrorValue(field, errorType));
            }
        }

        return result;
    }

}
