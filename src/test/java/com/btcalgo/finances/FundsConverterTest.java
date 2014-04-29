package com.btcalgo.finances;

import com.btcalgo.model.IOrderBook;
import com.btcalgo.service.marketdata.IMarketDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.btcalgo.finances.FundsEnum.*;
import static com.btcalgo.model.SymbolEnum.*;
import static com.btcalgo.util.Precision.EPSILON;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FundsConverterTest {

    private static final String MARKET = "BTCE";

    @Mock
    private IMarketDataProvider mdp;

    @Mock
    private FeeInfo feeInfo;

    private FundsConverter c;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        c = new FundsConverter(mdp, feeInfo);
    }

    /**
     * USD --> BTC
     * BTC --> USD
     */
    @Test
    public void testSingleConversions() {
        IOrderBook bookBtcUsd = mock(IOrderBook.class);

        when(feeInfo.getFee(BTCUSD)).thenReturn(10d);
        when(mdp.getIOrderBook(MARKET, BTCUSD)).thenReturn(bookBtcUsd);
        when(bookBtcUsd.getBestAskPrice()).thenReturn(400d);
        when(bookBtcUsd.getBestBidPrice()).thenReturn(500d);

        assertEquals(1.8, c.convertWithFee(BTC, USD, 1000), EPSILON);
        assertEquals(900, c.convertWithFee(USD, BTC, 2.5), EPSILON);
    }

    /**
     * XPM --> BTC --> EUR
     * EUR --> BTC --> XPM
     */
    @Test
    public void testMultipleConversions() {
        IOrderBook bookXpmBtc = mock(IOrderBook.class);
        IOrderBook bookBtcEur = mock(IOrderBook.class);

        when(feeInfo.getFee(XPMBTC)).thenReturn(10d);
        when(feeInfo.getFee(BTCEUR)).thenReturn(20d);

        when(mdp.getIOrderBook(MARKET, XPMBTC)).thenReturn(bookXpmBtc);
        when(mdp.getIOrderBook(MARKET, BTCEUR)).thenReturn(bookBtcEur);

        when(bookXpmBtc.getBestAskPrice()).thenReturn(0.0008);
        when(bookXpmBtc.getBestBidPrice()).thenReturn(0.001);

        when(bookBtcEur.getBestAskPrice()).thenReturn(300d);
        when(bookBtcEur.getBestBidPrice()).thenReturn(400d);

        assertEquals(172800, c.convertWithFee(EUR, XPM, 1_000_000), EPSILON);
        assertEquals(1800, c.convertWithFee(XPM, EUR, 1_000), EPSILON);
    }

}
