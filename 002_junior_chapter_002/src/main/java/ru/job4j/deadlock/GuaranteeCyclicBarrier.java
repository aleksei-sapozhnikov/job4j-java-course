package ru.job4j.deadlock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class GuaranteeCyclicBarrier {
    private CyclicBarrier counter = new CyclicBarrier(2);

    public static void main(String[] args) {
        GuaranteeCyclicBarrier main = new GuaranteeCyclicBarrier();
        Worker first = main.new Worker();
        Worker second = main.new Worker();
        new Thread(() -> {
            try {
                first.method(second, main.counter);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                second.method(first, main.counter);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private class Worker {

        private synchronized void method(Worker other, CyclicBarrier counter) throws InterruptedException, BrokenBarrierException {
            counter.await();
            other.method(this, counter);
        }

    }
}