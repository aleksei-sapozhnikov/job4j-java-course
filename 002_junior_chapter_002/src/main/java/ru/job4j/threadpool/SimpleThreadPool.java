package ru.job4j.threadpool;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.util.concurrent.ConcurrentLinkedQueue;

class SimpleThreadPool {
    /**
     * Flag showing if this SimpleThreadPool is running or not.
     */
    private boolean isRunning;
    /**
     * Array of working threads. Array size is determined
     * by number of available processors.
     */
    private final Thread[] threads = new Thread[Runtime.getRuntime().availableProcessors()];
    /**
     * Blocking queue of Runnable jobs.
     */
    private final ConcurrentLinkedQueue<Work> works = new ConcurrentLinkedQueue<>();

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
        synchronized (this.works) {
            this.works.add(work);
            System.out.format("-- Pool: added new %s. Queue size: %s.%n", work.getName(), this.works.size());
            this.works.notify();
        }
    }

    /**
     * Starts all threads to do works in queue.
     */
    void start() {
        this.isRunning = true;
        for (Thread thread : this.threads) {
            thread.start();
        }
        System.out.format("==== Started all threads.%n");
    }

    /**
     * Stops ThreadPool and notifies all threads.
     */
    void stop() {
        try {
            System.out.format("%n==== Pool: STOPPING all threads : time out.%n");
            this.isRunning = false;
            synchronized (this.works) {
                this.works.notifyAll();
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
                while (isRunning) {
                    System.out.format("---- %s: isRunning == TRUE, new cycle.%n", Thread.currentThread().getName());
                    synchronized (works) {
                        while (isRunning && works.isEmpty()) {
                            System.out.format("------ %s: queue size is %s, waiting for work.%n", Thread.currentThread().getName(), works.size());
                            works.wait();
                        }
                    }
                    System.out.format("-------- %s: queue size is %s, trying to take a new work.%n", Thread.currentThread().getName(), works.size());
                    Work next = works.poll();
                    if (next != null) {
                        System.out.format("---------- %s: took and started %s.%n", Thread.currentThread().getName(), next.getName());
                        next.doWork();
                        System.out.format("---------- %s: finished %s.%n", Thread.currentThread().getName(), next.getName());
                    } else {
                        System.out.format("---------- %s: next work == NULL.%n", Thread.currentThread().getName());
                    }
                }
                System.out.format("-- %s: isRunning == false, STOPPED.%n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}