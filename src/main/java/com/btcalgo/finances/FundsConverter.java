package com.btcalgo.finances;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.marketdata.IMarketDataProvider;

import static com.btcalgo.finances.FundsEnum.BTC;

public class FundsConverter {

    private static final String MARKET = "BTCE";

    private IMarketDataProvider mpd;
    private FeeInfo feeInfo;

    public FundsConverter(IMarketDataProvider marketDataProvider, FeeInfo feeInfo) {
        this.mpd = marketDataProvider;
        this.feeInfo = feeInfo;
    }

    public double convertWithFee(FundsEnum toCcy, FundsEnum fromCcy, double amount) {
        if (toCcy == fromCcy) {
            return amount;
        } else if (isSingleConversionPossible(toCcy, fromCcy)) {
            return doSingleConversion(toCcy, fromCcy, amount);
        } else if (isConversionThroughBtcPossible(toCcy, fromCcy)) {
            return doConversionThroughBTC(toCcy, fromCcy, amount);
        } else {
            return doMultipleConversion(toCcy, fromCcy, amount);
        }
    }

    public double convertWithoutFee(FundsEnum toCcy, FundsEnum fromCcy, double amount) {
        // TODO implement ?
        return 0;
    }

    /**
     * for toCcy=USD, fromCcy=BTC conversion pair is BTCUSD.
     * to get USD from BTC we need to SELL BTCUSD. We'll treat it as indirect conversion pair.
     */
    private double doSingleConversion(FundsEnum toCcy, FundsEnum fromCcy, double amount) {
        SymbolEnum conversionPair = getConversionPair(toCcy, fromCcy);
        boolean isDirectConversionPair = conversionPair.getSecond().equals(toCcy.name());
        double fee = feeInfo.getFee(conversionPair);
        double price;

        IOrderBook book = mpd.getIOrderBook(MARKET, conversionPair);
        if (book == null) {
            return 0;
        }

        if (isDirectConversionPair) {
            price = book.getBestAskPrice();
        } else {
            price = 1 / book.getBestBidPrice();
        }
        return amount * (1 - fee / 100) * price;
    }

    private double doConversionThroughBTC(FundsEnum toCcy, FundsEnum fromCcy, double amount) {
        return doSingleConversion(toCcy, BTC, doSingleConversion(BTC, fromCcy, amount));
    }

    @SuppressWarnings("UnusedParameters")
    private double doMultipleConversion(FundsEnum toCcy, FundsEnum fromCcy, double amount) {
        throw new IllegalArgumentException("No conversion defined for toCcy= " + toCcy + " and fromCcy=" + fromCcy);
    }

    private SymbolEnum getConversionPair(FundsEnum toCcy, FundsEnum fromCcy) {
        String pattern1 = toCcy.name() + fromCcy.name();
        String pattern2 = fromCcy.name() + toCcy.name();
        for (SymbolEnum symbolEnum : SymbolEnum.values()) {
            if (symbolEnum.name().equals(pattern1) || symbolEnum.name().equals(pattern2)) {
                return symbolEnum;
            }
        }
        throw new IllegalArgumentException("No conversion pair for toCcy=" + toCcy + " and fromCcy=" + fromCcy);
    }

    private boolean isSingleConversionPossible(FundsEnum toCcy, FundsEnum fromCcy) {
        String pattern1 = toCcy.name() + fromCcy.name();
        String pattern2 = fromCcy.name() + toCcy.name();
        for (SymbolEnum symbolEnum : SymbolEnum.values()) {
            if (symbolEnum.name().equals(pattern1) || symbolEnum.name().equals(pattern2)) {
                return true;
            }
        }
        return false;
    }

    private boolean isConversionThroughBtcPossible(FundsEnum toCcy, FundsEnum fromCcy) {
        return isSingleConversionPossible(BTC, fromCcy)
                && isSingleConversionPossible(toCcy, BTC);
    }

}
