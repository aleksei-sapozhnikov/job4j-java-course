package ru.job4j.streams.tracker;

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

    /**
     * Ask question and get answer from user what to do.
     * Answer must be an integer in a definite range.
     *
     * @param question Question we ask.
     * @param range    Answer number must be in that range.
     * @return Number of the action chosen by user.
     */
    public int ask(String question, int[] range) {
        int key = Integer.valueOf(this.ask(question));
        boolean inside = false;
        for (int item : range) {
            if (key == item) {
                inside = true;
                break;
            }
        }
        if (inside) {
            return key;
        } else {
            throw new MenuActionOutOfRangeException("Entered menu action is out of allowed range");
        }
    }

}
