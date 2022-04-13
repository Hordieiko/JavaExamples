package com.hord.thread;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ProducerConsumer {

    private static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) {
        MessageHolder holder = new MessageHolder();
        new Thread(new Producer(holder)).start();
        new Thread(new Consumer(holder)).start();
    }

    private static class Producer implements Runnable {
        final MessageHolder holder;
        final Random random = new Random();

        Producer(MessageHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            String[] messages = {
                    "One",
                    "Two",
                    "Three",
                    "Four",
                    "Five"
            };

            for (String message : messages) {
                holder.put(message);
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    logger.info("Sleep interrupted!");
                    Thread.currentThread().interrupt();
                }
            }
            holder.put("Finish");
        }
    }

    private static class Consumer implements Runnable {
        final MessageHolder holder;
        final Random random = new Random();

        Consumer(MessageHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            for (String message = holder.get(); !"Finish".equals(message); message = holder.get()) {
                logger.info("Message: " + message);
                try {
                    TimeUnit.MILLISECONDS.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    logger.info("Sleep interrupted!");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static class MessageHolder {
        String message;
        volatile boolean empty = true;

        synchronized void put(String message) {
            while (!empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.info("Put interrupted!");
                    Thread.currentThread().interrupt();
                }
            }

            this.message = message;
            this.empty = false;
            notifyAll();
        }

        synchronized String get() {
            while (empty) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    logger.info("Get interrupted!");
                    Thread.currentThread().interrupt();
                }
            }

            this.empty = true;
            notifyAll();

            return message;
        }
    }
}
