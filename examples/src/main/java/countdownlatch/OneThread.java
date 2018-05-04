package countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * А здесь один поток Worker уводит весь CountDownLatch в ноль.
 */
public class OneThread {
    public static void main(String[] args) throws InterruptedException {
        int N = 5;
        CountDownLatch latch = new CountDownLatch(N);
        new Thread(() -> {
            for (int i = 0; i < N; i++) {
                System.out.println("Worker: doing countdown, latch count left: " + latch.getCount());
                latch.countDown();
            }
        }).start();
        System.out.println(">>>>> Main: waiting");
        latch.await();
        System.out.println(">>>>> Main: finished!");
    }


}
