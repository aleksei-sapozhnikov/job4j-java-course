package threadinrunnable;

public class Runner implements Runnable {

    private final Thread thread = new Thread(this);
    private volatile boolean work = true;

    public void run() {
        try {
            while (work) {
                Thread.sleep(3600_000);
            }
        } catch (InterruptedException e) {
            System.out.println("Runner: caught InterruptedException, exiting.");
        } finally {
            this.work = false;
        }
    }

    public void start() {
        System.out.println("Runner: start()");
        this.thread.start();
        System.out.println("Runner: started.");
    }

    public void stop() {
        System.out.println("Runner: stop()");
        this.work = false;
        this.thread.interrupt();
    }
}
