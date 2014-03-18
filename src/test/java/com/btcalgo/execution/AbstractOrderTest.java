package com.btcalgo.execution;

import com.btcalgo.model.Direction;
import com.btcalgo.model.IOrderBook;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.ui.model.OrderDataHolder;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.btcalgo.util.DoubleFormatter.fmt;
import static com.btcalgo.util.Precision.EPSILON;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractOrderTest {

    protected Logger log = LoggerFactory.getLogger(getClass());
    private SymbolEnum EUR_USD = SymbolEnum.EURUSD;

    @Mock
    private OrdersManager ordersManager;

    private Order order;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public void createOrder(StrategyType type, Direction direction, double stopPrice, double limitPrice) {
        createOrder(type, direction, stopPrice, limitPrice, Double.NaN);
    }

    public void createOrder(StrategyType type, Direction direction, double stopPrice, double limitPrice, double offset) {
        OrderDataHolder holder = Mockito.mock(OrderDataHolder.class);
        when(holder.getStrategyType()).thenReturn(type);
        when(holder.getDirection()).thenReturn(direction);
        when(holder.getStopPrice()).thenReturn(stopPrice);
        when(holder.getLimitPrice()).thenReturn(limitPrice);
        when(holder.getSymbol()).thenReturn(EUR_USD);

        if (!Double.isNaN(offset)) {
            when(holder.getOffset()).thenReturn(offset);
        }

        Order order = null;
        if (holder.getStrategyType() == StrategyType.STOP_LOSS) {
            order = new StopOrder(ordersManager, holder);
        } else if (holder.getStrategyType() == StrategyType.TRAILING_STOP) {
            order = new TrailingStopOrder(ordersManager, holder);
        } else {
            log.error("Unknown strategy type: {}", holder.getStrategyType());
        }

        assertNotNull("Order shouldn't be null", order);

        assertEquals(type, order.getStrategyType());
        assertEquals(direction, order.getDirection());
        assertEquals(stopPrice, order.getStopPriceAsDouble(), EPSILON);
        assertEquals(limitPrice, order.getLimitPriceAsDouble(), EPSILON);

        if (type == StrategyType.TRAILING_STOP) {
            TrailingStopOrder trailingStopOrder = (TrailingStopOrder) order;
            assertEquals(offset, trailingStopOrder.getOffset(), EPSILON);
            assertEquals(limitPrice - stopPrice, trailingStopOrder.getLimitPriceOffset(), EPSILON);

            assertEquals(fmt(stopPrice), order.getInitialStopPrice());
            assertEquals(fmt(limitPrice), order.getInitialLimitPrice());
        } else {
            assertNull(order.getInitialStopPrice());
            assertNull(order.getInitialLimitPrice());
        }

        this.order = order;
    }

    public void checkState(double stopPrice, double limitPrice) {
        assertEquals(stopPrice, order.getStopPriceAsDouble(), EPSILON);
        assertEquals(limitPrice, order.getLimitPriceAsDouble(), EPSILON);

        assertEquals(fmt(stopPrice), order.getStopPrice());
        assertEquals(fmt(limitPrice), order.getLimitPrice());
    }

    public boolean checkRule(double bestBid, double bestAsk) {
        IOrderBook book = mock(IOrderBook.class);
        when(book.getBestBidPrice()).thenReturn(bestBid);
        when(book.getBestAskPrice()).thenReturn(bestAsk);

        return order.checkRule(book);
    }



}
