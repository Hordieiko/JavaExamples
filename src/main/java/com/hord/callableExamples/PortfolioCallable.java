package com.hord.callableExamples;

import java.util.concurrent.Callable;
import java.util.stream.IntStream;

public class PortfolioCallable implements Callable<Integer> {
    @Override
    public Integer call() {
        IntStream.rangeClosed(1, 10).forEach(a -> {
            try {
                Thread.sleep(400);
                System.out.println("PortfolioCallable " + a);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        return 10;
    }
}
