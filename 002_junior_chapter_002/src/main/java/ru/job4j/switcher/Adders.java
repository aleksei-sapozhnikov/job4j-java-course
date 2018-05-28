package ru.job4j.switcher;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Creates two threads appending values to an inner string holder. Threads are "first" and "second" and they
 * work in cycle: first-second-first-second-etc...
 * Every thread appends some integer value to a holder (as a string: 4 -> "4") for a given amount of times: "4444".
 * <p>
 * The first thread begins appending, the second appends after the first. For example, if first is adding 1 for 2 times
 * and second is adding 2 for 3 times, the result after 3 cycles will be:
 * 112221122211222
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 17.05.2018
 */
public class Adders {
    /**
     * Holder where the final string is held.
     */
    private final StringHolder holder;
    /**
     * First thread, appends first value.
     */
    private final Thread firstAdder;
    /**
     * Second thread, appends second value.
     */
    private final Thread secondAdder;
    /**
     * Counts cycles (cycle is "first thread made one iteration and second thread made one iteration". When
     * the first thread starts working again - it begins the next cycle.
     */
    private final AtomicInteger cyclesLeft;
    /**
     * Lock which will synchronize first and second adder threads.
     */
    private final ReentrantLock lock = new ReentrantLock();
    /**
     * Condition to synchronize threads: when the first is working and when the second.
     */
    private final Condition firstSecondSwitch = lock.newCondition();
    /**
     * Flag showing threads to continue or stop their work.
     */
    private volatile boolean working = true;
    /**
     * Flag which means tha now the first thread must work and append values to holder.
     * If <tt>false</tt> - means now the second thread should work.
     */
    private final AtomicBoolean addFirst = new AtomicBoolean(true);

    /**
     * Constructs new object with two threads which will append values.
     *
     * @param valueFirst  which value the first thread must append.
     * @param timesFirst  how much times the first value must repeat in one iteration.
     * @param valueSecond which value the second thread must append.
     * @param timesSecond how much times the second value must repeat in one iteration.
     * @param nCycles     how much "first-second" cycles should pass before the work will stop.
     */
    public Adders(StringHolder holder, int valueFirst, int timesFirst, int valueSecond, int timesSecond, int nCycles) {
        if (timesFirst < 0 || timesSecond < 0 || nCycles < 0) {
            throw new RuntimeException("Times and cycle must be not less then 0");
        }
        this.holder = holder;
        this.firstAdder = this.getAdderThread(valueFirst, timesFirst, true);
        this.secondAdder = this.getAdderThread(valueSecond, timesSecond, false);
        this.cyclesLeft = new AtomicInteger(nCycles);
    }

    /**
     * Returns thread which will append values (first or second).
     *
     * @param value   value to append.
     * @param times   how many times the value must be added in one iteration.
     * @param isFirst <tt>true</tt> means return the first thread, <tt>false</tt> - the second thread.
     * @return first or second thread adding values.
     */
    private Thread getAdderThread(int value, int times, boolean isFirst) {
        return new Thread(() -> {
            this.lock.lock();
            try {
                while (this.working && !Thread.currentThread().isInterrupted()) {
                    this.waitIfNeeded(isFirst);
                    this.addValuesIfPossible(value, times);
                    this.finishThreadAction(isFirst);
                }
            } catch (InterruptedException e) {
                this.working = false;
            } finally {
                this.lock.unlock();
            }
        });
    }

    /**
     * Checks if this thread should be working now and passes, or waits if another thread is working.
     *
     * @param toProceed condition value needed to proceed (true/false).
     * @throws InterruptedException if thread was interrupted while waiting.
     */
    private void waitIfNeeded(boolean toProceed) throws InterruptedException {
        while (this.addFirst.get() != toProceed) {
            this.firstSecondSwitch.await();
        }
    }

    /**
     * If there still is cycle to make - performs an appending iteration: appends given value
     * to a holder for needed amount of times.
     *
     * @param value value to append.
     * @param times how many times append this value.
     */
    private void addValuesIfPossible(int value, int times) {
        if (this.cyclesLeft.get() > 0) {
            for (int i = 0; i < times; i++) {
                this.holder.append(value);
            }
        } else {
            this.working = false;
        }
    }

    /**
     * Launches when thread finished adding its values. If this was the second
     * ("not the first") thread - counts "first-second" cycle. Then switches
     * boolean field to make another thread work - and wakes up another thread.
     */
    private void finishThreadAction(boolean isFirstAdder) {
        if (!isFirstAdder) {
            this.cyclesLeft.decrementAndGet();
        }
        this.addFirst.set(!this.addFirst.get());
        this.firstSecondSwitch.signal();
    }

    /**
     * Launches adder threads, waits for them to complete operations.
     *
     * @throws InterruptedException if threads were interrupted while waiting.
     */
    public void perform() throws InterruptedException {
        this.firstAdder.start();
        this.secondAdder.start();
        this.firstAdder.join();
        this.secondAdder.join();
    }
}
