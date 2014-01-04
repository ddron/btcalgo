package com.algo.btce.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.event.Event;
import reactor.function.Consumer;

@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
public class ThrowableHandler implements Consumer<Event<Throwable>> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void accept(Event<Throwable> throwableEvent) {
        Throwable throwable = throwableEvent.getData();
        log.error("Exception was thrown:", throwable);
    }
}
