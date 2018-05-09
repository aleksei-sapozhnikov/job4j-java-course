package executor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пробуем использовать ThreadPoolExecutor, изучаем его методы execute() и submit().
 * Синхронизируем методы при помощи "задвижек" - CountDownLatch.
 */
public class JustTry {
    private static volatile AtomicInteger i = new AtomicInteger(0);
    private int n = 5;
    private CountDownLatch latchVoid = new CountDownLatch(n);
    private CountDownLatch latchReturn = new CountDownLatch(n);

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private ExecutorService executor = new ThreadPoolExecutor(n, n, 500, TimeUnit.MILLISECONDS, queue);

    private Runnable voidTask = () -> {
        System.out.format("%s: executing Task \"%s\", returning void%n", Thread.currentThread().getName(), i.getAndIncrement());
        latchVoid.countDown();
    };

    private Callable<Integer> futTask = () -> {
        final int finalI = i.incrementAndGet();
        System.out.format("%s: executing Task \"%s\", returning Future with value \"%s\"%n", Thread.currentThread().getName(), finalI, finalI);
        latchReturn.countDown();
        return finalI;
    };


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        JustTry t = new JustTry();

        // Выполнение команд без возвращаемого значения - execute.
        System.out.println("=== EXECUTE ===");
        for (int i = 0; i < t.n; i++) {
            t.executor.execute(t.voidTask);
        }
        t.latchVoid.await();

        // Выполнение команды с возвращаемым значением - submit()
        System.out.println();
        System.out.println("=== SUBMIT ===");
        Future[] futures = new Future[t.n];
        for (int i = 0; i < t.n; i++) {
            futures[i] = t.executor.submit(t.futTask);
        }
        t.latchReturn.await();

        // Выключаем экзекутор и печатаем результат от submit.
        t.executor.shutdown();

        System.out.println();
        System.out.println("=== SUBMIT: RESULTS: ===");
        for (Future f : futures) {
            System.out.println("Figure = " + f.get());
        }
    }
}
