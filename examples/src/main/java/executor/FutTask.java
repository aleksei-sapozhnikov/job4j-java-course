package executor;

import java.util.concurrent.*;

public class FutTask {
    private ExecutorService executor = Executors.newCachedThreadPool();
    private CountDownLatch flag = new CountDownLatch(1);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutTask futTask = new FutTask();

        Callable<String> task = () -> {
            try {
                System.out.println("Task thread: " + Thread.currentThread().getName());
                return "TASK RESULT";
            } finally {
                futTask.flag.countDown();
            }
        };
        FutureTask<String> taskToUse = new FutureTask<>(task);

        System.out.println("Main thread: " + Thread.currentThread().getName());

// Далее три варианта, как запустить taskToUse. Выбираем один, остальные комментируем.

        // напрямую в том же потоке
        taskToUse.run();

        // через отдельный поток
//        new Thread(taskToUse).start();

        // через executor
//        System.out.println();
//        futTask.executor.execute(taskToUse);


        futTask.flag.await();

        System.out.println("Task is done? : " + taskToUse.isDone());
        String result = taskToUse.get();
        System.out.println("Result = " + result);

        futTask.executor.shutdown();
    }
}
