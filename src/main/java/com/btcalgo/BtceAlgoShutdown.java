package com.btcalgo;

import com.btcalgo.reactor.NameableConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;

public class BtceAlgoShutdown implements NameableConsumer<Event<Void>> {
    private static Logger log = LoggerFactory.getLogger(BtceAlgoShutdown.class);

    @Override
    public void accept(Event<Void> voidEvent) {
        doShutdown();
    }

    private void doShutdown() {
        log.info("Shutdown process started...");
        BtceAlgo.getApp().stop(); // app shutdown

        log.info("Shutdown process completed. Exiting...");
        System.exit(0);
    }

    @Override
    public String getId() {
        return "stop";
    }
}
