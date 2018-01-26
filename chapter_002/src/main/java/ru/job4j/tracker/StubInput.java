package ru.job4j.tracker;

/**
 * Stub to imitate console-based input.
 */
public class StubInput implements Input {

    /**
     * Answers that will be given.
     */
    private String[] answers;

    /**
     * Position of the next answer to use.
     */
    private int position = 0;

    /**
     * Constructor.
     *
     * @param answers Array of answers given by StubInput.
     */
    public StubInput(String[] answers) {
        this.answers = answers;
    }

    /**
     * Give next answer.
     *
     * @param question Question we ask.
     * @return Answer from "user".
     */
    public String ask(String question) {
        return (this.answers[position++]);
    }

}
