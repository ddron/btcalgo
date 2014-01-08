package com.btcalgo.model;

import com.btcalgo.service.marketdata.SymbolEnum;

public interface IOrderBook {
    String getMarket();

    SymbolEnum getSymbol();

    /**
     * @param direction direction of our side<br></br>
     *                  Direction = BID --> we're buying (need best bid price)<br></br>
     *                  Direction = ASK --> we're selling (need best ask price)<br></br>
     * @return best price for given direction
     */
    double getBestPrice(Direction direction);

    /**
     * @return best price at what we can BUY now
     */
    double getBestBidPrice();

    /**
     * @return best price at what we can SELL now
     */
    double getBestAskPrice();

}
