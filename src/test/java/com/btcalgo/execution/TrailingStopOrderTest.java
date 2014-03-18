package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class TrailingStopOrderTest extends AbstractOrderTest {

    @Test
    public void testBuyTrailingOrder() {
        double stopPrice = 9.2;
        double limitPrice = 9.4;
        double offset = 2.5;

        createOrder(StrategyType.TRAILING_STOP, Direction.BID, stopPrice, limitPrice, offset);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(8.4, 8.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(9.199, 8.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(8.1, 7.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(7.1, 6.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(6.7, 5.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(6.5, 5.2));
        checkState(9.0, 9.2);

        assertFalse(checkRule(6.0, 5.2));
        checkState(8.5, 8.7);

        assertFalse(checkRule(5.5, 5.2));
        checkState(8.0, 8.2);

        assertFalse(checkRule(6.5, 5.2));
        checkState(8.0, 8.2);

        assertFalse(checkRule(7.5, 5.2));
        checkState(8.0, 8.2);

        assertFalse(checkRule(7.99, 5.2));
        checkState(8.0, 8.2);

        assertTrue(checkRule(8.0, 5.2));
        checkState(8.0, 8.2);

        assertTrue(checkRule(9.0, 5.2));
        checkState(8.0, 8.2);
    }

    @Test
    public void testBuyTrailingOrderWithNegativeLimitPriceOffset() {
        double stopPrice = 10;
        double limitPrice = 8;
        double offset = 3;

        createOrder(StrategyType.TRAILING_STOP, Direction.BID, stopPrice, limitPrice, offset);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(7.4, 6.5));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(6.7, 6.3));
        checkState(9.7, 7.7);

        assertFalse(checkRule(4.5, 2.3));
        checkState(7.5, 5.5);

        assertFalse(checkRule(7.2, 7.1));
        checkState(7.5, 5.5);

        assertTrue(checkRule(7.5, 5.2));
        checkState(7.5, 5.5);

        assertTrue(checkRule(9.0, 8.2));
        checkState(7.5, 5.5);
    }

    @Test
    public void testSellTrailingOrder() {
        double stopPrice = 10.5;
        double limitPrice = 9.5;
        double offset = 2.5;

        createOrder(StrategyType.TRAILING_STOP, Direction.ASK, stopPrice, limitPrice, offset);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(12, 11.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(12, 10.501));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(15, 14.2));
        checkState(11.7, 10.7);

        assertFalse(checkRule(15, 11.8));
        checkState(11.7, 10.7);

        assertFalse(checkRule(15, 13.8));
        checkState(11.7, 10.7);

        assertFalse(checkRule(15, 14.8));
        checkState(12.3, 11.3);

        assertTrue(checkRule(15, 12.3));
        checkState(12.3, 11.3);

        assertTrue(checkRule(15, 10.3));
        checkState(12.3, 11.3);
    }

    @Test
    public void testSellTrailingOrderWithNegativeLimitPriceOffset() {
        double stopPrice = 10;
        double limitPrice = 12;
        double offset = 3;

        createOrder(StrategyType.TRAILING_STOP, Direction.ASK, stopPrice, limitPrice, offset);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(12, 11.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(13.8, 12.8));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(15.1, 13.1));
        checkState(10.1, 12.1);

        assertFalse(checkRule(15.3, 14.5));
        checkState(11.5, 13.5);

        assertFalse(checkRule(12.7, 11.7));
        checkState(11.5, 13.5);

        assertTrue(checkRule(13.5, 11.5));
        checkState(11.5, 13.5);

        assertTrue(checkRule(11.5, 10.5));
        checkState(11.5, 13.5);
    }

}
