package threadinrunnable;

public class Main {
    private final Runner runner = new Runner();

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        System.out.println("Main: starting runner.");
        main.runner.start();
        System.out.println("Main: started runner, waiting...");
        Thread.sleep(5000);
        System.out.println("Main: stopping runner.");
        main.runner.stop();
    }
}
