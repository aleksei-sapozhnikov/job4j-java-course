package threadlocal;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Мы создаем поле String name - которое ThreadLocal.
 * <p>
 * Далее в методе main мы запускаем потоки, где обращаемся к этому name и присваиваем ему значение.
 * Однако, значение name для каждого потока будет свое - поскольку ThreadLocal делает
 * локальную копию переменной (и ее объекта) для каждого потока.
 */
public class Counter {
    private CyclicBarrier barrier = new CyclicBarrier(3);
    private ThreadLocal<String> name = new ThreadLocal<>();

    private Runnable runner = () -> {
        try {
            name.set(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + " waiting main");
            System.out.flush();
            barrier.await();
            System.out.println(Thread.currentThread().getName() + " name: " + name.get());
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
    };

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        Counter tt = new Counter();
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
