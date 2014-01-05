package com.algo.btce.reactor;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;
import reactor.event.dispatch.ThreadPoolExecutorDispatcher;

public class ReactorFactory {

    public static Reactor createReactor(Environment environment) {
        ThreadPoolExecutorDispatcher dispatcher = new ThreadPoolExecutorDispatcher(32, 128);
        return Reactors.reactor()
                .env(environment)
                .dispatcher(dispatcher)
                .get();
    }
}

