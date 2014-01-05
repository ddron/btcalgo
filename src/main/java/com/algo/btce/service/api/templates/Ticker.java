package com.algo.btce.service.api.templates;

import com.algo.btce.model.BestOrderBook;

public class Ticker {
    private static final String MARKET = "BTCE";

    private String symbol;

    private double high;
    private double low;
    private double avg;
    private double vol;
    private double vol_cur;
    private double last;
    private double buy; // highest buy price currently on market (best for us). i.e. 'market buys' at this price
    private double sell; // lowest sell price currently on market (best for us) i.e. 'market sells' at this price

    private long updated;
    private long server_time;

    public BestOrderBook convertToBestOrderBook() {
        return new BestOrderBook(MARKET, symbol, buy, sell);
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public double getVol_cur() {
        return vol_cur;
    }

    public void setVol_cur(double vol_cur) {
        this.vol_cur = vol_cur;
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public double getBuy() {
        return buy;
    }

    public void setBuy(double buy) {
        this.buy = buy;
    }

    public double getSell() {
        return sell;
    }

    public void setSell(double sell) {
        this.sell = sell;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getServer_time() {
        return server_time;
    }

    public void setServer_time(long server_time) {
        this.server_time = server_time;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
