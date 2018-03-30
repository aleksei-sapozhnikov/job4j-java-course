package ru.job4j.problems;

public class RacerWorker implements Runnable {

    private int number;

    private String[] professions;

    private String[] duties;

    RacerWorker(int number, String[] professions, String[] duties) {
        this.number = number;
        this.professions = professions;
        this.duties = duties;
    }

    public void run() {
        try {
            System.out.printf("Профессия №%s: ", this.number);
            Thread.sleep(50);
            System.out.print(this.professions[number]);
            Thread.sleep(50);
            System.out.println(this.duties[number]);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
