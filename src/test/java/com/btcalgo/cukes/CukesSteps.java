package com.btcalgo.cukes;

import com.btcalgo.BtceAlgo;
import com.btcalgo.execution.Order;
import com.btcalgo.execution.OrdersManager;
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
    }

    @Then(value = "^put new ([^\"]*) \"([^\"]*)\" order \"([^\"]*)\" of \"([^\"]*)\" with stopPrice = \"([^\"]*)\"$", timeout = STEP_TIMEOUT)
    public void put_new_order_for_with_stopPrice(String side, String orderType, final String amountAndPrice, String pair,
             final String stopPriceAsString) throws Throwable {
        double amount = Double.valueOf(amountAndPrice.split("@")[0]);
        double limitPrice = Double.valueOf(amountAndPrice.split("@")[1]);
        double stopPrice = Double.valueOf(stopPriceAsString);

        final SymbolEnum symbol = SymbolEnum.valueOf(pair);
        final Direction direction = Direction.valueByApiValue(side);

        final MainPageController mainPageController = BtceAlgo.getApp().getContext().getBean(MainPageController.class);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
                    && symbol == order.getSymbol()) {
                log.info("Success. Order created: {}", order);
                orderFound = true;
                break;
            }
        }

        if (!orderFound) {
            Assert.fail("no order has been created");
        }
    }


    private MockedApiService getApiService() {
        return BtceAlgo.getApp().getContext().getBean(MockedApiService.class);
    }
}
