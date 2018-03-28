package ru.job4j.interrupt;

class CountTime implements Runnable {

    private String input;

    private long maxTime;

    private long start;

    CountTime(String input, int maxTime) {
        this.input = input;
        this.maxTime = maxTime;
        this.start = System.currentTimeMillis();
    }

    @Override
    public void run() {
        Thread charThread = new Thread(new CountChars(this.input));
        charThread.start();
        while (charThread.isAlive()) {
            long time = this.time();
            if (time > this.maxTime) {
                charThread.interrupt();
                System.out.printf("Interrupting thread, time passed : %s%n", time);
                break;
            }
        }
    }

    private long time() {
        return System.currentTimeMillis() - this.start;
    }

}
