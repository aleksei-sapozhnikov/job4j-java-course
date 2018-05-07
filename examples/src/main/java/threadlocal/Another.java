package threadlocal;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Толком не очень понятно, что тут хотели показать. Видимо, что у каждого потока
 * есть своя переменная, и он может присваивать ей значение.
 */
public class Another {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        class MyRunnable implements Runnable {

            @Override
            public void run() {
                lock.lock();
                try {
                    String name = Thread.currentThread().getName();
                    System.out.println(name + " first threadLocal: "
                            + threadLocal.get());
                    threadLocal.set(name + " thread value");
                    System.out.println(name + " end threadLocal: "
                            + threadLocal.get());
                } finally {
                    lock.unlock();
                }
            }
        }

        threadLocal.set("From main thread");
        Thread thread1 = new Thread(new MyRunnable(), "first_thread");
        thread1.start();
        Thread thread2 = new Thread(new MyRunnable(), "second_thread");
        thread2.start();
        thread1.join();
        thread2.join();

        System.out.println("fromMainThread: " + threadLocal.get());
    }
}