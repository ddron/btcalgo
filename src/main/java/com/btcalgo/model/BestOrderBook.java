package com.btcalgo.model;

public class BestOrderBook implements IOrderBook {

    private String market;
    private String symbol;

    /**
     * lowest sell price currently on market i.e. 'market sells' at this price.<br></br>
     * So it's the best price for us to BUY (when doing BID)
     */
    private double bestBuy;

    /**
     * highest buy price currently on market i.e. 'market buys' at this price<br></br>
     * So it's the best price for us to SELL (when doing ASK)
     */
    private double bestSell;

    public BestOrderBook(String market, String symbol, double bestBuy, double bestSell) {
        this.market = market;
        this.symbol = symbol;
        this.bestBuy = bestBuy;
        this.bestSell = bestSell;
    }

    @Override
    public String getMarket() {
        return market;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public double getBestPrice(Direction direction) {
        return direction == Direction.BID ? getBestBidPrice() : getBestAskPrice();
    }

    @Override
    public double getBestBidPrice() {
        return bestBuy;
    }

    @Override
    public double getBestAskPrice() {
        return bestSell;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BestOrderBook{");
        sb.append("market='").append(market).append('\'');
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", bestBuy=").append(bestBuy);
        sb.append(", bestSell=").append(bestSell);
        sb.append('}');
        return sb.toString();
    }
}
