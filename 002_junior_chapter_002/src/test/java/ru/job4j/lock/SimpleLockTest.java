package ru.job4j.lock;

import org.junit.Test;

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