package barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Здесь второй тред встает на очередь к барьеру, а затем его прерывают. Тогда барьер выкидывает исключение:
 * "или все или никто", и больше уже ни один тред встать на очередь не может.
 */
public class IfInterrupted {
    public static void main(String[] args) throws InterruptedException {
        int nWorkers = 3;
        CyclicBarrier barrier = new CyclicBarrier(nWorkers);

        class Worker extends Thread {
            private final String name;

            Worker(String name) {
                this.name = name;
            }

            public void run() {
                try {
                    System.out.println(name + ": started, launching await");
                    System.out.flush();
                    barrier.await();
                    System.out.println(name + ": FINISHED");
                    System.out.flush();
                } catch (InterruptedException e) {
                    System.out.println(">>> " + name + ": interrupted");
                    System.out.flush();
                } catch (BrokenBarrierException e) {
                    System.out.println(">>> " + name + ": barrier broken, exiting");
                    System.out.flush();
                }
            }
        }

        Thread w1 = new Worker("First");
        Thread w2 = new Worker("Second");
        Thread w3 = new Worker("Third");
        Thread w4 = new Worker("Fourth");

        System.out.format("=== Launching threads ===%n");
        System.out.flush();
        w1.start();
        Thread.sleep(1000);
        w2.start();     // second wait...
        w2.interrupt(); // ... no, get away!
        Thread.sleep(1000);
        w3.start();
        Thread.sleep(1000);
        w4.start();
        Thread.sleep(1000);
        // start
        System.out.format("%n=== Finally: now waiting threads: %s%n", barrier.getNumberWaiting());
        System.out.flush();
    }
}

