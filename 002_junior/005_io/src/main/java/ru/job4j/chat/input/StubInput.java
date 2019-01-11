package ru.job4j.chat.input;

import java.util.List;

/**
 * Stub input for tests.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version 0.1
 * @since 0.1
 */
public class StubInput implements Input {

    /**
     * List of consecutive inputs.
     */
    private final List<String> inputs;

    /**
     * Index of the next input to use.
     */
    private int index = 0;

    /**
     * Constructs new instance.
     *
     * @param inputs Consecutive inputs.
     */
    public StubInput(String... inputs) {
        this.inputs = List.of(inputs);
    }

    /**
     * Returns next input.
     *
     * @param invite Invite message for input.
     * @return Next input.
     */
    @Override
    public String get(String invite) {
        return this.inputs.get(index++);
    }
}
