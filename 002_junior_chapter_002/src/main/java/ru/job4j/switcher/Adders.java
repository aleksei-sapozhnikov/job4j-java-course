package ru.job4j.switcher;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Adders {
    private final Thread firstAdder;
    private final Thread secondAdder;
    private final CountDownLatch limiter;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstSecond = lock.newCondition();
    private final StringHolder holder = new StringHolder();
    private boolean working = true;
    private boolean addFirst = true;

    public Adders(int valueFirst, int timesFirst, int valueSecond, int timesSecond, int nCycles) {
        this.limiter = new CountDownLatch(nCycles);
        this.firstAdder = this.getAdderThread(valueFirst, timesFirst, true);
        this.secondAdder = this.getAdderThread(valueSecond, timesSecond, false);
    }

    public static void main(String[] args) throws InterruptedException {
        Adders adders = new Adders(1, 1, 2, 2, 4);
        adders.perform();
        System.out.println(adders.holderValueToString());
    }

    private Thread getAdderThread(int value, int times, boolean isFirstAdder) {
        return new Thread(() -> {
            this.lock.lock();
            try {
                while (this.working && !Thread.currentThread().isInterrupted()) {
                    this.waitIfNeeded(isFirstAdder);
                    this.addValuesIfAllowed(value, times);
                    this.completeThreadAction();
                    this.countCycle(isFirstAdder);
                }
            } catch (InterruptedException e) {
                this.working = false;
            } finally {
                this.lock.unlock();
            }
        });
    }

    private void completeThreadAction() {
        this.addFirst = !this.addFirst;
        this.firstSecond.signal();
    }

    private void countCycle(boolean isFirstAdder) {
        if (!isFirstAdder) {
            this.limiter.countDown();
        }
    }

    private void waitIfNeeded(boolean toProceed) throws InterruptedException {
        while (this.addFirst != toProceed) {
            firstSecond.await();
        }
    }

    private void addValuesIfAllowed(int value, int times) {
        if (this.limiter.getCount() > 0) {
            for (int i = 0; i < times; i++) {
                this.holder.append(value);
            }
        } else {
            this.working = false;
        }
    }

    public void perform() throws InterruptedException {
        this.firstAdder.start();
        this.secondAdder.start();
        this.firstAdder.join();
        this.secondAdder.join();
    }

    public String holderValueToString() {
        return this.holder.toString();
    }
}
