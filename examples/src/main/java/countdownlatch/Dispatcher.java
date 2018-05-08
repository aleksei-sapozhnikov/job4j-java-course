package countdownlatch;

import java.util.concurrent.CountDownLatch;


/**
 * Пример, как организовать работу потоков, используя два CountDownLatch. Запускаются треды-рабочие, которые встают в
 * ожидание с помощью startSignal.await(). Сам startSignal ожидает лишь одного вызова countDown(), чтобы запуститься.
 * Когда драйвер вызывает startSignal.countDown(), то все рабочие принимаются за работу. Драйвер же становится на ожидание
 * stopSignal.await().
 * <p>
 * Каждый рабочий, закончив работу, вызывает stopSignal.countDown(). При этом счетчик в stopSignal уменьшается на единицу.
 * Когда последний рабочий закончил работу, он дергает countDown(), счетчик становится равным нулю, и stopSignal запускает
 * драйвера, который и заканчивает работу.
 */
public class Dispatcher {
    public static void main(String[] args) throws InterruptedException {
        Driver driver = new Driver(5);
        driver.main();
    }
}

class Driver {
    private final int nWorkers;

    public Driver(int nWorkers) {
        this.nWorkers = nWorkers;
    }

    void main() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(nWorkers);
        // create and start threads
        for (int i = 0; i < nWorkers; ++i) {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        System.out.println("Before countdown");         // don't let run yet
        startSignal.countDown();            // let all threads proceed
        System.out.println("After countdown, before awake");
        doneSignal.await();                 // wait for all to finish
        System.out.println("After await");
    }
}

class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException ex) {
        } // return;
    }

    void doWork() {
        System.out.println("Worker " + Thread.currentThread().getId() + " doing some work");
    }
}