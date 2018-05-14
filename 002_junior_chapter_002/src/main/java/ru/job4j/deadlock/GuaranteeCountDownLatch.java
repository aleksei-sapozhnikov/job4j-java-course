package ru.job4j.deadlock;

import java.util.concurrent.CountDownLatch;

public class GuaranteeCountDownLatch {
    private CountDownLatch counter = new CountDownLatch(2);

    public static void main(String[] args) {
        GuaranteeCountDownLatch main = new GuaranteeCountDownLatch();
        Worker first = main.new Worker();
        Worker second = main.new Worker();
        new Thread(() -> {
            try {
                first.method(second, main.counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                second.method(first, main.counter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class Worker {

        private synchronized void method(Worker other, CountDownLatch counter) throws InterruptedException {
            counter.countDown();
            counter.await();
            other.method(this, counter);
        }

    }
}
