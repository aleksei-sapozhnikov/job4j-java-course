package threadlocal;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Мы создаем поле String counter - которое ThreadLocal.
 * <p>
 * Далее в методе main мы запускаем потоки, где обращаемся к этому counter и присваиваем ему значение.
 * Однако, значение counter для каждого потока будет свое - поскольку ThreadLocal делает
 * локальную копию переменной (и ее объекта) для каждого потока.
 */
public class TryThreadLocal {
    private CyclicBarrier barrier = new CyclicBarrier(3);
    private ThreadLocal<String> counter = new ThreadLocal<>();

    Runnable runner = () -> {
        try {
            counter.set(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + " waiting main");
            System.out.flush();
            barrier.await();
            System.out.println(Thread.currentThread().getName() + " counter: " + counter.get());
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        TryThreadLocal tt = new TryThreadLocal();
        Thread a1 = new Thread(tt.runner);
        Thread a2 = new Thread(tt.runner);
        a1.start();
        a2.start();
        System.out.println("Main: waiting threads");
        System.out.flush();
        tt.barrier.await();
        System.out.println("Main: barrier passed");
        System.out.flush();
        a1.join();
        a2.join();
    }


}
