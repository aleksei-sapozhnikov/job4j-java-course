package ru.job4j.tracker;

/**
 * Interface to get input data from user.
 */
public interface Input {

    /**
     * Ask question and get answer from user.
     *
     * @param question Question we ask.
     * @return Answer given by user.
     */
    String ask(String question);

}
