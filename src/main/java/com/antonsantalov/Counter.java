package com.antonsantalov;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    private static AtomicInteger counter = new AtomicInteger(0);

    public void incrementCounter() {
        counter.incrementAndGet();
    }
}
