package ru.job4j.tracker;

/**
 * Interface to get input data from user.
 */
public interface Input {

    /**
     * Ask question and get answer from user.
     *
     * @param question Question we ask.
     * @return User's answer.
     */
    String ask(String question);

    /**
     * Ask question and get answer from user what to do.
     * Answer must be an integer in a definite range.
     *
     * @param question Question we ask.
     * @param range    Answer number must be in that range.
     * @return Number (key) of the action chosen by user.
     */
    int ask(String question, int[] range);

}
