package ru.job4j.lock;

/**
 * Simple lock to synchronize threads.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 12.04.2018
 */
public class SimpleLock {
    /**
     * Locker object for synchronisation.
     */
    private final Object lock = new Object();
    /**
     * Thread that owns this SimpleLock.
     */
    private Thread exclusiveOwnerThread = null;

    /**
     * Locks this lock.
     */
    public void lock() {
        System.out.format("%s: LOCK TRY%n", Thread.currentThread().getName());
        try {
            synchronized (this.lock) {
                while (this.exclusiveOwnerThread != null) {
                    System.out.format("%s: LOCK BUSY, WAIT%n", Thread.currentThread().getName());
                    this.lock.wait();
                }
                System.out.format("%s: LOCK ACQUIRED!%n", Thread.currentThread().getName());
                this.exclusiveOwnerThread = Thread.currentThread();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unlocks this lock.
     */
    public void unlock() {
        this.exclusiveOwnerThread = null;
        synchronized (this.lock) {
            this.exclusiveOwnerThread = null;
            System.out.format("%s: LOCK RELEASED!%n", Thread.currentThread().getName());
            this.lock.notify();
        }
    }

}
