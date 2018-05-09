package executor;

import java.util.concurrent.*;

public class FutTask {
    private ExecutorService executor = Executors.newCachedThreadPool();
    private CountDownLatch flag = new CountDownLatch(1);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutTask futTask = new FutTask();

        Callable<String> task = () -> {
            try {
                System.out.println("executing");
                return "TASK RESULT";
            } finally {
                futTask.flag.countDown();
            }
        };
        FutureTask<String> taskToUse = new FutureTask<>(task);
        futTask.executor.execute(taskToUse);
        futTask.flag.await();

        System.out.println("Task is done? : " + taskToUse.isDone());
        String result = taskToUse.get();
        System.out.println("Result = " + result);

        futTask.executor.shutdown();
    }
}
