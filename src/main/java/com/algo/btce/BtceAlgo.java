package com.algo.btce;

import com.algo.btce.reactor.NameableConsumer;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import reactor.core.Reactor;

import static reactor.event.selector.Selectors.$;

public class BtceAlgo {
    private AbstractApplicationContext context;

    private final Object lock = new Object();
    private volatile boolean finished = false;

    private static BtceAlgo app;

    public static void main(String[] args) throws InterruptedException {
        app = new BtceAlgo();
        app.start();
    }

    private void start() throws InterruptedException {
        context = new ClassPathXmlApplicationContext("btce-algo-config.xml");
        context.registerShutdownHook();

        makeConsumerRegistrations();
        invokeCommandLineProcessor();

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

    private void invokeCommandLineProcessor() {
        Reactor reactor = context.getBean(Reactor.class);
        ConsoleCommandProcessor commandProcessor = context.getBean(ConsoleCommandProcessor.class);

        reactor.notify(commandProcessor.getId());
    }

    private void makeConsumerRegistrations() {
        Reactor reactor = context.getBean(Reactor.class);

        for (NameableConsumer consumer : context.getBeansOfType(NameableConsumer.class).values()) {
            reactor.on($(consumer.getId()), consumer);
        }
    }
}
