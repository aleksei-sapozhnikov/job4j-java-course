package ru.job4j.interrupt;

/**
 * Runs thread and interrupts it if it works too long.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 28.03.2018
 */
class Time implements Runnable {
    /**
     * Input string for the CountChars thread.
     */
    private String input;
    /**
     * Maximum time waiting in milliseconds.
     */
    private long maxTime;

    /**
     * Constructs an instance for thread.
     *
     * @param input   input string for CountChars.
     * @param maxTime maximum working time in milliseconds.
     */
    Time(String input, int maxTime) {
        this.input = input;
        this.maxTime = maxTime;
    }

    /**
     * Runs in a thread.
     */
    @Override
    public void run() {
        Thread charThread = new Thread(new CountChars(this.input));
        charThread.start();
        long started = System.currentTimeMillis();
        while (charThread.isAlive() && !charThread.isInterrupted()) {
            long time = System.currentTimeMillis() - started;
            if (time > this.maxTime) {
                charThread.interrupt();
                System.out.printf("Interrupting thread, time passed : %s%n", time);
                break;
            }
        }
    }
}
