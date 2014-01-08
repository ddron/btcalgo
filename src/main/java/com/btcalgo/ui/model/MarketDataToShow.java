package com.btcalgo.ui.model;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.marketdata.IMarketDataListener;
import com.btcalgo.service.marketdata.IMarketDataProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import reactor.event.Event;

import static com.btcalgo.util.DoubleFormatter.fmt;

public class MarketDataToShow implements IMarketDataListener, ChangeListener<String> {
    private static final String MARKET = "BTCE";

    private StringProperty bestBidPrice = new SimpleStringProperty(); // best price for us to BUY
    private StringProperty bestAskPrice = new SimpleStringProperty(); // best price for us to SELL
    private SymbolEnum selectedSymbol;

    private IMarketDataProvider marketDataProvider;

    public MarketDataToShow(IMarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
    }

    @Override
    public String getId() {
        return "marketDataToShow";
    }

    @Override
    public void accept(Event<IOrderBook> iOrderBookEvent) {
        IOrderBook book = iOrderBookEvent.getData();
        if (book != null && MARKET.equals(book.getMarket()) && (selectedSymbol == book.getSymbol())) {
            setBestBidPrice(book.getBestBidPrice());
            setBestAskPrice(book.getBestAskPrice());
        }
    }

    @Override
    public void changed(ObservableValue<? extends String> observableValue, String oldDisplayValue, String newDisplayValue) {
        SymbolEnum newSymbolEnum = SymbolEnum.valueByDisplayName(newDisplayValue);
        if (newSymbolEnum != selectedSymbol) {
            bestBidPrice.set("");
            bestAskPrice.set("");

            marketDataProvider.removeListener(this, MARKET, selectedSymbol);
            selectedSymbol = newSymbolEnum;

            IOrderBook book = marketDataProvider.getIOrderBook(MARKET, selectedSymbol);
            if (book != null) {
                setBestBidPrice(book.getBestBidPrice());
                setBestAskPrice(book.getBestAskPrice());
            }
            marketDataProvider.addListener(this, MARKET, selectedSymbol);
        }
    }

    public String getBestBidPrice() {
        return bestBidPrice.get();
    }

    public StringProperty bestBidPriceProperty() {
        return bestBidPrice;
    }

    public void setBestBidPrice(double bestBidPrice) {
        this.bestBidPrice.set(fmt(bestBidPrice));
    }

    public String getBestAskPrice() {
        return bestAskPrice.get();
    }

    public StringProperty bestAskPriceProperty() {
        return bestAskPrice;
    }

    public void setBestAskPrice(double bestAskPrice) {
        this.bestAskPrice.set(fmt(bestAskPrice));
    }

}
