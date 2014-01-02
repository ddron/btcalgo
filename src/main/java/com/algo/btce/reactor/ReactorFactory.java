package com.algo.btce.reactor;

import reactor.core.Environment;
import reactor.core.Reactor;
import reactor.core.spec.Reactors;

public class ReactorFactory {

    public static Reactor createReactor(Environment environment) {
        return Reactors.reactor()
                .env(environment)
                .dispatcher(Environment.THREAD_POOL)
                .get();
    }
}

