package ru.job4j.interrupt;

/**
 * Counts chars in given input, can be used in thread.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 28.03.2018
 */
class CountChars extends Thread {
    /**
     * Input string.
     */
    private String input;

    /**
     * Constructs object with input string.
     *
     * @param input input string.
     */
    CountChars(String input) {
        this.input = input;
    }

    /**
     * Runs thread.
     */
    @Override
    public void run() {
        System.out.printf("Chars : %s", this.countChars());
    }

    /**
     * Counts number of chars in given string.
     *
     * @return number of chars in input string.
     */
    int countChars() {
        char[] chars = this.input.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (Thread.interrupted()) {
                System.out.println("Thread interrupted while working");
                break;
            }
            count++;
            System.out.printf("Counted char %s, count : %s%n", c, count);
            this.wasteTime();                       // Просто чтобы тред занимал хоть какое-то время.
        }
        return count;
    }

    /**
     * Just wastes time sleeping.
     */
    private void wasteTime() {
        try {
            sleep(1000);
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted while sleeping");
            ie.printStackTrace();
        }
    }

}
