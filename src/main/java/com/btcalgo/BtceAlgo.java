package com.btcalgo;

import com.btcalgo.model.SymbolEnum;
import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.reactor.Scheduler;
import com.btcalgo.reactor.ThrowableHandler;
import com.btcalgo.service.LicenseService;
import com.btcalgo.service.RuntimeMeter;
import com.btcalgo.service.api.ActiveOrdersService;
import com.btcalgo.service.api.FeeService;
import com.btcalgo.service.api.FundsService;
import com.btcalgo.service.marketdata.BtcBestMarketDataSource;
import com.btcalgo.ui.TabsManager;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import reactor.core.Environment;
import reactor.core.Reactor;

import java.util.Random;
import java.util.concurrent.ScheduledFuture;

import static reactor.event.selector.Selectors.$;
import static reactor.event.selector.Selectors.T;

public class BtceAlgo extends Application {
    private static Logger log = LoggerFactory.getLogger(BtceAlgo.class);

    private AbstractApplicationContext context;

/*    private final Object lock = new Object();
    private volatile boolean finished = false;*/

    private static BtceAlgo app;
    private static final long MD_UPDATE_RATE_MS = 400;
    private static final long FEE_UPDATE_RATE_MS = 55_000;

    private static String CONTEXT_FILE_NAME = "btcalgo-context.xml";

    public static void main(String[] args) throws InterruptedException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        log.info("Starting btce algo...");

        app = this;
        // start spring backend
        startContainer();

        // start ui frontend
        context.getBean(TabsManager.class).init(stage);

        log.info("btce algo started!");
/*        // wait :-)
        while (!finished) {
            synchronized (lock) {
                lock.wait();
            }
        }*/
    }

    private void startContainer() throws InterruptedException {
        context = new ClassPathXmlApplicationContext(CONTEXT_FILE_NAME);
        context.registerShutdownHook();

        makeConsumerRegistrations();
        invokeCommandLineProcessor();
        setUpMarketDataUpdating();
        setUpFeeUpdating();
        setUpMeter();
        setUpFundService();
        setUpFundActiveOrdersService();
    }

    @Override
    public void stop() {
        try {
            super.stop();
        } catch (Exception e) {
            log.error("Exception during javaFx app stop: ", e);
        }

        context.getBean(Environment.class).shutdown(); // reactor shutdown
        context.close(); // spring container shutdown
/*        finished = true;

        synchronized (lock) {
            lock.notify();
        }*/
    }

    private void setUpMarketDataUpdating() {
        BtcBestMarketDataSource marketDataSource = context.getBean(BtcBestMarketDataSource.class);
        Scheduler scheduler = context.getBean(Scheduler.class);

        for (SymbolEnum symbolEnum : SymbolEnum.values()) {
            scheduler.scheduleAtFixedRate(marketDataSource.getId(), symbolEnum, MD_UPDATE_RATE_MS);
        }
    }

    private void setUpFeeUpdating() {
        Random r = new Random();
        FeeService feeService = context.getBean(FeeService.class);
        Scheduler scheduler = context.getBean(Scheduler.class);

        for (SymbolEnum symbolEnum : SymbolEnum.values()) {
            // we need this random number to reduce simultaneous threads usage.
            // At the moment we've got about 25 ccy pairs
            scheduler.scheduleAtFixedRate(feeService.getId(), symbolEnum, FEE_UPDATE_RATE_MS + r.nextInt(5_000));
        }
    }

    private void setUpMeter() {
        LicenseService licenseService = context.getBean(LicenseService.class);
        if (!licenseService.hasValidLicense()) {
            RuntimeMeter meter = context.getBean(RuntimeMeter.class);
            Scheduler scheduler = context.getBean(Scheduler.class);

            ScheduledFuture scheduledFuture = scheduler.scheduleAtFixedRate(meter.getId(), "", 60_000);
            meter.setScheduledFuture(scheduledFuture);
        }
    }

    private void setUpFundService() {
        FundsService fundsService = context.getBean(FundsService.class);
        Scheduler scheduler = context.getBean(Scheduler.class);

        scheduler.scheduleAtFixedRate(fundsService.getId(), "", 10_000);
    }

    private void setUpFundActiveOrdersService() {
        ActiveOrdersService activeOrdersService = context.getBean(ActiveOrdersService.class);
        Scheduler scheduler = context.getBean(Scheduler.class);

        scheduler.scheduleAtFixedRate(activeOrdersService.getId(), "", 10_000);
    }

    private void invokeCommandLineProcessor() {
        Reactor reactor = context.getBean(Reactor.class);
        ConsoleCommandProcessor commandProcessor = context.getBean(ConsoleCommandProcessor.class);

        reactor.notify(commandProcessor.getId());
    }

    private void makeConsumerRegistrations() {
        Reactor r = context.getBean(Reactor.class);

        for (NameableConsumer consumer : context.getBeansOfType(NameableConsumer.class).values()) {
            r.on($(consumer.getId()), consumer);
        }

        r.on(T(Throwable.class), new ThrowableHandler());
    }

    public static BtceAlgo getApp() {
        return app;
    }

    public static void setContextFileName(String contextFileName) {
        CONTEXT_FILE_NAME = contextFileName;
    }

    public AbstractApplicationContext getContext() {
        return context;
    }
}
