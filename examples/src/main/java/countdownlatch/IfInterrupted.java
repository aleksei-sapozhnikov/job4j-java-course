package countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * Здесь несколько тредов встают в ожидание сигнала. Затем второй тред прерывают, и он уходит.
 * Но все остальные остаются (в отличие от Cyclic Barrier, где больше ни один тред не может
 * встать в очередь).
 */
public class IfInterrupted {
    public static void main(String[] args) throws InterruptedException {
        int N = 1;
        CountDownLatch latch = new CountDownLatch(N);

        class Worker extends Thread {
            private final String name;

            Worker(String name) {
                this.name = name;
            }

            public void run() {
                try {
                    System.out.println(name + ": waiting");
                    System.out.flush();
                    latch.await();
                    System.out.println(name + ": FINISHED");
                    System.out.flush();
                } catch (InterruptedException e) {
                    System.out.println(">>> " + name + ": interrupted");
                    System.out.flush();
                }
            }
        }

        Thread w1 = new Worker("First");
        Thread w2 = new Worker("Second");
        Thread w3 = new Worker("Third");
        Thread w4 = new Worker("Fourth");

        System.out.format("%n=== Launching threads ===%n");
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
        System.out.format("%n=== Finally: now threads left to start: %s ===%n", latch.getCount());
        System.out.format("%n=== Main thread: triggering threads ===%n");
        System.out.flush();
        latch.countDown();
    }
}
