package com.btcalgo.service.api.templates;

import com.btcalgo.model.BestOrderBook;

import java.util.Date;

public class Ticker {
    private static final String MARKET = "BTCE";

    private String symbol;

    private double high;
    private double low;
    private double avg;
    private double vol;
    private double vol_cur;
    private double last;

    /**
     * lowest sell price currently on market i.e. 'market sells' at this price.<br></br>
     * So it's the best price for us to BUY (when doing BID)
     */
    private double buy;

    /**
     * highest buy price currently on market i.e. 'market buys' at this price<br></br>
     * So it's the best price for us to SELL (when doing ASK)
     */
    private double sell;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ticker{");
        sb.append("high=").append(high);
        sb.append(", low=").append(low);
        sb.append(", avg=").append(avg);
        sb.append(", vol=").append(vol);
        sb.append(", vol_cur=").append(vol_cur);
        sb.append(", last=").append(last);
        sb.append(", buy=").append(buy);
        sb.append(", sell=").append(sell);
        sb.append(", updated=").append(new Date(updated));
        sb.append(", server_time=").append(new Date(server_time));
        sb.append('}');
        return sb.toString();
    }
}
