package com.algo.btce;

import com.algo.btce.reactor.NameableConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Environment;
import reactor.event.Event;

public class BtceAlgoShutdown implements NameableConsumer<Event<Void>> {
    private static Logger log = LoggerFactory.getLogger(BtceAlgoShutdown.class);

    private Environment env;

    public BtceAlgoShutdown(Environment env) {
        this.env = env;
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        doShutdown();
    }

    private void doShutdown() {
        log.info("Shutdown process started...");
        env.shutdown();  // reactor shutdown
        BtceAlgo.stop(); // app shutdown

        log.info("Shutdown process completed. Exiting...");
        System.exit(0);
    }

    @Override
    public String getId() {
        return "stop";
    }
}
