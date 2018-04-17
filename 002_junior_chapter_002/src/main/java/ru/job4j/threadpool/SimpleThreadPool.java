package ru.job4j.threadpool;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class SimpleThreadPool {
    /**
     * Array of working threads. Array size is determined
     * by number of available processors.
     */
    private final Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
    /**
     * Blocking queue of Runnable jobs.
     */
    private final BlockingQueue<Work> works = new LinkedBlockingQueue<>();

    /**
     * Constructs and initializes new SimpleThreadPool.
     */
    SimpleThreadPool() {
        for (int i = 0; i < this.threads.length; i++) {
            this.threads[i] = new WorkerThread();
        }
        System.out.format("==== Number of processors: %s%n", Runtime.getRuntime().availableProcessors());
        System.out.format("==== Number of threads: %s%n", this.threads.length);
    }

    /**
     * Adds work to queue and notifies waiting threads.
     *
     * @param work work to add.
     */
    void add(Work work) {
        this.works.add(work);
        System.out.format("-- Pool: added new %s, queue size now: %s.%n", work.getName(), this.works.size());
    }

    /**
     * Starts all threads to do works in queue.
     */
    void start() {
        for (Thread thread : this.threads) {
            thread.start();
        }
    }

    /**
     * Stops ThreadPool and notifies all threads to make them stop.
     */
    void stop() {
        try {
            System.out.format("%n==== Pool: stopping all threads : time out.%n");
            for (Thread thread : this.threads) {
                thread.interrupt();
            }
            for (Thread thread : this.threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Working thread: takes Work from queue and runs it.
     */
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    this.tryDoNextWork(works);
                }
            } catch (InterruptedException e) {
                System.out.format("-- %s: caught \"interrupted\" exception, stopping.%n", Thread.currentThread().getName());
            }
            System.out.format("-- %s: STOPPED%n", Thread.currentThread().getName());
        }

        /**
         * Tries to take next work in queue.
         */
        private void tryDoNextWork(BlockingQueue<Work> works) throws InterruptedException {
            System.out.format("-------- %s: trying to take a new work, queue size now: %s.%n", Thread.currentThread().getName(), works.size());
            Work next = works.take();
            if (next != null) {
                System.out.format("---------- %s: took and started %s, queue size now: %s.%n", Thread.currentThread().getName(), next.getName(), works.size());
                next.doWork();
                System.out.format("---------- %s: finished %s.%n", Thread.currentThread().getName(), next.getName());
            } else {
                System.out.format("---------- %s: next work == NULL, skipping.%n", Thread.currentThread().getName());
            }
        }
    }


}