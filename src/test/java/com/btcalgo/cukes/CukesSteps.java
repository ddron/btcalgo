package com.btcalgo.cukes;

import com.btcalgo.BtceAlgo;
import com.btcalgo.execution.Order;
import com.btcalgo.execution.OrderStatus;
import com.btcalgo.execution.OrdersManager;
import com.btcalgo.execution.StrategyType;
import com.btcalgo.model.Direction;
import com.btcalgo.model.SymbolEnum;
import com.btcalgo.service.api.MockedApiService;
import com.btcalgo.ui.MainPageController;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javafx.application.Platform;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static com.btcalgo.util.Precision.areEqual;

public class CukesSteps {

    private String contextFileName = "btcalgo-test-context.xml";

    private static final long TIME_TO_START_BTCALGO_MS = 10_000;
    private static final long STEP_TIMEOUT = 5_000;

    private Logger log = LoggerFactory.getLogger(getClass());

    @After
    public void stop(){
        BtceAlgo.getApp().stop();
    }

    @Given(value = "^btcalgo engine$", timeout = STEP_TIMEOUT + TIME_TO_START_BTCALGO_MS)
    public void btcalgo_engine() throws Throwable {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BtceAlgo.setContextFileName(contextFileName);
                    BtceAlgo.main(new String[]{});
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }).start();

        Thread.sleep(TIME_TO_START_BTCALGO_MS);
    }

    @Given("^has valid keys$")
    public void has_valid_keys() throws Throwable {
        MockedApiService apiService = getApiService();
        apiService.setValidKeys(true);
    }

    @Given(value = "^md for \"([^\"]*)\" is buy=\"([^\"]*)\", sell=\"([^\"]*)\"$", timeout = STEP_TIMEOUT)
    public void md_for_is_buy_sell(String pair, String buyPrice, String sellPrice) throws Throwable {
        MockedApiService apiService = getApiService();
        apiService.setNewTicker(pair, buyPrice, sellPrice);
        Thread.sleep(500); // wait for md delivery
    }

    @Then(value = "^put new ([^\"]*) \"([^\"]*)\" order \"([^\"]*)\" of \"([^\"]*)\" with stopPrice = \"([^\"]*)\"$", timeout = STEP_TIMEOUT)
    public void put_new_order_for_with_stopPrice(String side, final String orderType, final String amountAndPrice, String pair,
             final String stopPriceAsString) throws Throwable {
        double amount = Double.valueOf(amountAndPrice.split("@")[0]);
        double limitPrice = Double.valueOf(amountAndPrice.split("@")[1]);
        double stopPrice = Double.valueOf(stopPriceAsString);

        final SymbolEnum symbol = SymbolEnum.valueOf(pair);
        final Direction direction = Direction.valueByApiValue(side);
        StrategyType strategyType = StrategyType.valueByDisplayName(orderType);

        final MainPageController mainPageController = BtceAlgo.getApp().getContext().getBean(MainPageController.class);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainPageController.setStrategyTypeValue(orderType);
                mainPageController.setAmountValue(amountAndPrice.split("@")[0]);
                mainPageController.setLimitPriceValue(amountAndPrice.split("@")[1]);
                mainPageController.setStopPriceValue(stopPriceAsString);
                mainPageController.setDirectionValue(direction.getDisplayName());
                mainPageController.setSelectedPair(symbol.getDisplayName());
                mainPageController.handleSubmit(null);
            }
        });
        Thread.sleep(200); // wait for order creation

        // check that order created
        OrdersManager ordersManager = BtceAlgo.getApp().getContext().getBean(OrdersManager.class);
        boolean orderFound = false;

        for (Order order : ordersManager.getOrders()) {
            if (areEqual(order.getAmount(), amount)
                    && areEqual(order.getLimitPrice(), limitPrice)
                    && areEqual(order.getStopPrice(), stopPrice)
                    && symbol == order.getSymbol()
                    && direction == order.getDirection()
                    && strategyType == order.getStrategyType()
                    && order.getStatus() == OrderStatus.WAITING) {
                log.info("Success. Order created: {}", order);
                orderFound = true;
                break;
            }
        }

        if (!orderFound) {
            Assert.fail("no order has been created");
        }
    }

    @Then(value = "^([^\"]*) order \"([^\"]*)\" of \"([^\"]*)\" sent to market$", timeout = STEP_TIMEOUT)
    public void order_of_sent_to_market(String side, String amountAndPrice, String pair) throws Throwable {
        Direction direction = Direction.valueByApiValue(side);
        double amount = Double.valueOf(amountAndPrice.split("@")[0]);
        double limitPrice = Double.valueOf(amountAndPrice.split("@")[1]);
        SymbolEnum symbol = SymbolEnum.valueOf(pair);

        BlockingQueue<Order> sentOrders = getApiService().getSentOrders();
        boolean orderFound = false;

        while (!orderFound) {
            Order order = sentOrders.take();
            if (areEqual(order.getAmount(), amount)
                    && areEqual(order.getLimitPrice(), limitPrice)
                    && symbol == order.getSymbol()
                    && direction == order.getDirection()) {
                log.info("Success. Order sent to market: {}", order);
                orderFound = true;
            }
        }
    }

    private MockedApiService getApiService() {
        return BtceAlgo.getApp().getContext().getBean(MockedApiService.class);
    }
}
