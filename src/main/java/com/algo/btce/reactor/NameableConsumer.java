package com.algo.btce.reactor;

import reactor.function.Consumer;

public interface NameableConsumer<T> extends Consumer<T> {

    String getId();

}
