package ru.job4j.lock;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for SimpleLock class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.04.2018
 */
public class SimpleLockTest {

    /**
     * Test lock() and unlock() methods in multi-threading.
     */
    @Test
    public void whenMultipleThreadsThenWorkOneByOne() {
        try {
            int nThreads = 10;
            SimpleLock lock = new SimpleLock();
            Thread[] workers = new Thread[nThreads];
            for (int i = 0; i < nThreads; i++) {
                workers[i] = new LockingWorker(lock);
            }
            for (Thread worker : workers) {
                worker.start();
            }
            for (Thread worker : workers) {
                worker.join();
            }
            System.out.format("%n=== PROGRAM FINISHED ===%n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void whenOtherThreadTriesUnlockThenException() throws InterruptedException {
        boolean[] wasException = {false};
        SimpleLock lock = new SimpleLock();
        Thread locker = new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread unlocker = new Thread(() -> {
            try {
                Thread.sleep(2500);
                System.out.format("%s: TRYING UNLOCK%n", Thread.currentThread().getName());
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IllegalMonitorStateException e) {
                System.out.format("%s: caught %s%n", Thread.currentThread().getName(), e.getClass().getName());
                wasException[0] = true;
            }
        });
        locker.start();
        unlocker.start();
        locker.join();
        unlocker.join();
        assertThat(wasException[0], is(true));
    }

    /**
     * Class of threads using the same SimpleLock object to synchronize.
     */
    private class LockingWorker extends Thread {
        /**
         * Lock object: can be use by only one thread at once.
         */
        private final SimpleLock lock;

        /**
         * Constructs new object.
         *
         * @param lock lock object, the same for all threads we need to synchronize.
         */
        LockingWorker(SimpleLock lock) {
            this.lock = lock;
        }

        /**
         * Runs thread.
         */
        @Override
        public void run() {
            try {
                System.out.format(">>> %s: START%n", Thread.currentThread().getName());
                this.lock.lock();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.format(">>> %s: FINISH%n", Thread.currentThread().getName());
                this.lock.unlock();
            }
        }
    }
}