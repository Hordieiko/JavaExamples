package com.hord.thread;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class ThreadJoinTest extends Thread {

    public static void main(String[] args) throws InterruptedException {
        new ThreadJoinTest("first").start();
        Thread second = new ThreadJoinTest("second");
        second.start(); // - if thread is not alive, method join() will be immediately returned!!!
        second.join();
        System.out.println("Finish");
    }

    String name;

    private ThreadJoinTest(String name) {
        this.name = name;
    }

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " [name: " + name + "]");
        TimeUnit.SECONDS.sleep(2);
    }
}
