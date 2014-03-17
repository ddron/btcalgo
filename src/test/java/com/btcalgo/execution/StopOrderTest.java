package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class StopOrderTest extends AbstractOrderTest {

    @Test
    public void testBuyStopOrder() {
        double stopPrice = 9.2;
        double limitPrice = 9.4;

        createOrder(StrategyType.STOP_LOSS, Direction.BID, stopPrice, limitPrice);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(8.4, 8.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(9.1, 8.2));
        checkState(stopPrice, limitPrice);

        assertTrue(checkRule(9.2, 8.2));
        checkState(stopPrice, limitPrice);

        assertTrue(checkRule(9.9, 8.2));
        checkState(stopPrice, limitPrice);
    }

    @Test
    public void testSellStopOrder() {
        double stopPrice = 10.5;
        double limitPrice = 9.5;

        createOrder(StrategyType.STOP_LOSS, Direction.ASK, stopPrice, limitPrice);
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(12, 11.2));
        checkState(stopPrice, limitPrice);

        assertFalse(checkRule(12, 10.501));
        checkState(stopPrice, limitPrice);

        assertTrue(checkRule(12, 10.5));
        checkState(stopPrice, limitPrice);

        assertTrue(checkRule(12, 9.1));
        checkState(stopPrice, limitPrice);
    }
}
