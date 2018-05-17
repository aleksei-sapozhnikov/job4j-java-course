package ru.job4j.deadlock;

import java.util.concurrent.CountDownLatch;

/**
 * Make guaranteed deadlock using synchronized methods and CountDownLatch.
 */
public class GuaranteeCountDownLatch {
    /**
     * Launches two threads which will come to deadlock.
     *
     * @param args cmd arguments.
     */
    public static void main(String[] args) {
        GuaranteeCountDownLatch main = new GuaranteeCountDownLatch();
        CountDownLatch counter = new CountDownLatch(2);
        Worker first = main.new Worker();
        Worker second = main.new Worker();
        new Thread(() -> main.doMethods(first, second, counter)).start();
        new Thread(() -> main.doMethods(second, first, counter)).start();
    }

    /**
     * Calls two methods which use first and second worker (made to avoid try/catch in main method).
     *
     * @param first   first worker.
     * @param second  second worker/
     * @param counter latch to synchronize threads work.
     */
    private void doMethods(Worker first, Worker second, CountDownLatch counter) {
        try {
            first.firstMethod(second, counter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Worker thread class.
     */
    private class Worker {
        /**
         * Blocks "this" worker, waits when "other" worker will block himself.
         * Then tries to call method on the "other" worker which leads to a dead block.
         *
         * @param other   second worker.
         * @param counter latch to synchronize threads.
         * @throws InterruptedException if thread was interrupted while waiting.
         */
        private synchronized void firstMethod(Worker other, CountDownLatch counter) throws InterruptedException {
            counter.countDown();
            counter.await();
            other.secondMethod(this);
        }

        /**
         * Method is called when object is blocked in the firstMethod, so it shouldn't work.
         *
         * @param worker worker to print hashcode of.
         */
        private synchronized void secondMethod(Worker worker) {
            System.out.println(worker.hashCode());
        }
    }
}
