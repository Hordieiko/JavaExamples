package com.hord.callableExamples;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCallable {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Future<Integer>> futures = new ArrayList<>();
        List<Integer> results = new ArrayList<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            futures.add(executor.submit(new PortfolioCallable()));
            futures.add(executor.submit(new MarketNewsCallable()));

            // There is we are waiting until all Callable threads are finished.
            for (Future<Integer> future : futures) {
                results.add(future.get());
            }
        } finally {
            if (Objects.nonNull(executor))
                try {
                    executor.shutdown();
                } catch (Exception e) {
                    System.out.println("Can't shut down ExecutorService");
                    e.printStackTrace();
                }
        }

        System.out.println("The end");
    }
}
