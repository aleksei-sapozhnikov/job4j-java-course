package threadlocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Просто пробуем синтаксис такой штуки как ThreadLocal переменная.
 */
public class TryThreadLocal {
    private ThreadLocal<SimpleDateFormat> dateFormat = ThreadLocal.withInitial(SimpleDateFormat::new);
    private ThreadLocal<ConcurrentLinkedDeque<String>> queue = ThreadLocal.withInitial(ConcurrentLinkedDeque::new);

    private Runnable forDate = () -> {
        for (int i = 0; i < 10; i++) {
            String s = this.dateFormat.get().format(new Date());
            System.out.format("%s: %s%n", Thread.currentThread().getName(), s);
        }
    };

    private Runnable forQueue = () -> {
        ConcurrentLinkedDeque<String> q = this.queue.get();
        for (int i = 0; i < 10; i++) {
            q.offer(Thread.currentThread().getName());
        }
        System.out.println(q);
    };

    public static void main(String[] args) throws InterruptedException {
        TryThreadLocal ex = new TryThreadLocal();

        System.out.println("=== Trying with SimpleDate (from Horstmann) ===");
        new Thread(ex.forDate).start();
        new Thread(ex.forDate).start();

//        System.out.println();
//        System.out.println("=== LOCAL QUEUE - the same object?");
//        Thread a1 = new Thread(ex.forQueue);
//        Thread a2 = new Thread(ex.forQueue);
//        a1.start();
//        a2.start();
//        a1.join();
//        a2.join();
//        ConcurrentLinkedDeque h = ex.queue.get();
//        System.out.print("Main: ");
//        System.out.println(h);
    }
}
