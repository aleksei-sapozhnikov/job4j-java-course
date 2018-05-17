package ru.job4j.deadlock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Guaranteed deadlock using two locks and and cyclic barrier.
 */
public class GuaranteeCyclicBarrier {
    /**
     * Launches two threads which will lock their first lock, then will try
     * to lock the second (already locked by other thread) and go to deadlock.
     *
     * @param args cmd arguments.
     */
    public static void main(String[] args) {
        GuaranteeCyclicBarrier main = new GuaranteeCyclicBarrier();
        CyclicBarrier barrier = new CyclicBarrier(2);
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();
        new Thread(() -> main.lock(lock1, lock2, barrier)).start();
        new Thread(() -> main.lock(lock2, lock1, barrier)).start();
    }

    /**
     * Locks first lock, then awaits for other thread to lock the second thread.
     * Then tries to use second lock (and another thread tries the same). Deadlock.
     *
     * @param lock1   first lock.
     * @param lock2   second lock.
     * @param barrier barrier to synchronize locks.
     */
    private void lock(Lock lock1, Lock lock2, CyclicBarrier barrier) {
        try {
            lock1.lock();
            barrier.await();
            lock2.lock();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

}