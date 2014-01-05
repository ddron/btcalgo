package com.algo.btce.model;

public class BestOrderBook implements IOrderBook {

    private String market;
    private String symbol;

    private double bestBuy; // highest buy price currently on market (best for us). i.e. 'market buys' at this price
    private double bestSell; // lowest sell price currently on market (best for us) i.e. 'market sells' at this price

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
        return bestSell;
    }

    @Override
    public double getBestAskPrice() {
        return bestBuy;
    }
}
