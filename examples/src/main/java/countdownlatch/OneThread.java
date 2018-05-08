package countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * А здесь один поток Worker уводит весь CountDownLatch в ноль.
 */
public class OneThread {
    public static void main(String[] args) throws InterruptedException {
        int nWorkers = 5;
        CountDownLatch latch = new CountDownLatch(nWorkers);
        new Thread(() -> {
            for (int i = 0; i < nWorkers; i++) {
                System.out.println("Worker: doing countdown, latch count left: " + latch.getCount());
                latch.countDown();
            }
        }).start();
        System.out.println(">>>>> Main: waiting");
        latch.await();
        System.out.println(">>>>> Main: finished!");
    }


}
