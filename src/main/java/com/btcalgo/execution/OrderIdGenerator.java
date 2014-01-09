package com.btcalgo.execution;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderIdGenerator {
    private static final String BASE = "Order_";

    private static AtomicInteger orderNumber = new AtomicInteger(0);


    public static String nextId() {
        return BASE + System.currentTimeMillis() + "_" + orderNumber.getAndIncrement();
    }
}

