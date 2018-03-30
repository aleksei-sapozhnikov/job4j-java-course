package ru.job4j.problems;

/**
 * Thread giving information about professions with some "sleeps" during work.
 * To illustrate problems of race condition.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 30.03.2018
 */
public class RacerWorker implements Runnable {
    /**
     * Number of the profession and duty in given arrays.
     */
    private int number;
    /**
     * Array of professions.
     */
    private String[] professions;
    /**
     * Array of duties.
     */
    private String[] duties;

    /**
     * Constructs a new worker.
     *
     * @param number      number of the profession and duty.
     * @param professions list of professions.
     * @param duties      list of duties.
     */
    RacerWorker(int number, String[] professions, String[] duties) {
        this.number = number;
        this.professions = professions;
        this.duties = duties;
    }

    /**
     * Runs when called by Thread.start()
     */
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
