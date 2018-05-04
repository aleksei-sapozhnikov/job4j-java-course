package barrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Cyclic {
    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        int N = 5; // number of workers
        CyclicBarrier barrier = new CyclicBarrier(N + 1); // one more for main thread
        // making workers
        Runnable worker = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " started");
                System.out.flush();
                System.out.println(Thread.currentThread().getName() + " doing work");
                Thread.sleep(2000);
                System.out.flush();
                System.out.println(Thread.currentThread().getName() + " finished");
                System.out.flush();
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException ex) {
                ex.printStackTrace();
            }
        };
        // Run workers
        System.out.println(">>>>> Main thread: starting and waiting workers");
        System.out.flush();
        for (int i = 0; i < N; i++) {
            new Thread(worker).start();
        }
        // waiting workers
        barrier.await();
        System.out.println(">>>>> Main thread: stopped");
        System.out.flush();
    }
}