package com.btcalgo;

import com.btcalgo.reactor.NameableConsumer;
import com.btcalgo.reactor.Scheduler;
import com.btcalgo.reactor.ThrowableHandler;
import com.btcalgo.service.marketdata.BtcBestMarketDataSource;
import com.btcalgo.service.marketdata.SymbolEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import reactor.core.Reactor;

import java.util.Arrays;
import java.util.List;

import static reactor.event.selector.Selectors.$;
import static reactor.event.selector.Selectors.T;

public class BtceAlgo {
    private static Logger log = LoggerFactory.getLogger(BtceAlgo.class);

    private AbstractApplicationContext context;

    private final Object lock = new Object();
    private volatile boolean finished = false;

    private static BtceAlgo app;

    private static final long MD_UPDATE_RATE_MS = 200;
    private List<SymbolEnum> symbols = Arrays.asList(SymbolEnum.BTCUSD/*SymbolEnum.values()*/);

    public static void main(String[] args) throws InterruptedException {
        app = new BtceAlgo();
        app.start();
    }

    private void start() throws InterruptedException {
        log.info("Starting btce algo...");

        context = new ClassPathXmlApplicationContext("btce-algo-config.xml");
        context.registerShutdownHook();

        makeConsumerRegistrations();
        invokeCommandLineProcessor();
        setUpMarketDataUpdating();

        log.info("btce algo started!");
        while (!finished) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    public static void stop() {
        app.context.close(); // spring container shutdown
        app.finished = true;

        synchronized (app.lock) {
            app.lock.notify();
        }
    }

    private void setUpMarketDataUpdating() {
        BtcBestMarketDataSource marketDataSource = context.getBean(BtcBestMarketDataSource.class);
        Scheduler scheduler = context.getBean(Scheduler.class);

        for (SymbolEnum symbolEnum : symbols) {
            scheduler.scheduleAtFixedRate(marketDataSource.getId(), symbolEnum, MD_UPDATE_RATE_MS);
        }
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
}
