package com.hord.callableExamples;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class MarketNewsCallable implements Callable<Integer> {
    @Override
    public Integer call() {
        IntStream.rangeClosed(1, 10).forEach(a -> {
            try {
                Thread.sleep(900);
                System.out.println("MarketNewsCallable " + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return 123;
    }
}
