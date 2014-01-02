package com.algo.btce;

import com.algo.btce.reactor.NameableConsumer;
import reactor.core.Environment;
import reactor.event.Event;

public class BtceAlgoShutdown implements NameableConsumer<Event<Void>> {

    private Environment env;

    public BtceAlgoShutdown(Environment env) {
        this.env = env;
    }

    @Override
    public void accept(Event<Void> voidEvent) {
        doShutdown();
    }

    private void doShutdown() {
        System.out.println("Shutdown process started...");
        env.shutdown();  // reactor shutdown
        BtceAlgo.stop(); // app shutdown

        System.out.println("Shutdown process completed. Exiting...");
        System.exit(0);
    }

    @Override
    public String getId() {
        return "stop";
    }
}
