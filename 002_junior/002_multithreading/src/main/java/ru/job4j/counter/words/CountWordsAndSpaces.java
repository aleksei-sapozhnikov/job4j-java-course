package ru.job4j.counter.words;

/**
 * Counts words and spaces in given input.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.03.2018
 */
class CountWordsAndSpaces {
    /**
     * Input string.
     */
    private String input;

    /**
     * Constructs new object with given input string.
     *
     * @param input input string.
     */
    CountWordsAndSpaces(String input) {
        this.input = input;
    }

    /**
     * Starts multi-threaded operations counting words and spaces.
     */
    void start() {
        try {
            System.out.println("STARTING...");
            Thread space = new Thread(() -> System.out.printf("=== Words : %s%n", this.countWords()));
            Thread word = new Thread(() -> System.out.printf("=== Spaces : %s%n", this.countSpaces()));
            word.start();
            space.start();
            word.join();
            space.join();
        } catch (InterruptedException ie) {
            System.out.println("Thread interrupted!");
            ie.printStackTrace();
        }
        System.out.println("FINISHED!");
    }

    /**
     * Counts number of spaces in the input string.
     *
     * @return number of spaces.
     */
    int countSpaces() {
        char[] chars = this.input.toCharArray();
        int count = 0;
        for (char c : chars) {
            if (c == ' ') {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts number of words in given string.
     *
     * @return number of words.
     */
    int countWords() {
        char[] chars = this.input.toCharArray();
        int count = 0;
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == ' ' && chars[i - 1] != ' ') {
                count++;
            }
        }
        if (chars[chars.length - 1] != ' ') {       // last word
            count++;
        }
        return count;
    }
}
