package ru.job4j.tracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for ValidateInput class.
 */
public class ValidateInputTest {

    /**
     * Stores "standard" console output.
     */
    private final PrintStream stdout = System.out;

    /**
     * Output for testing.
     */
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();


    /**
     * Change "standard" output to testing output.
     */
    @Before
    public void changeOutput() {
        System.setOut(new PrintStream(this.out));
    }

    /**
     * Return output back to "standard".
     */
    @After
    public void backOutput() {
        System.setOut(this.stdout);
    }

    /**
     * Test ask method.
     */
    @Test
    public void whenInputNotNumberThenMessage() {
        ValidateInput input = new ValidateInput(
                new StubInput(new String[]{"invalid", "1"})
        );
        input.ask("Enter", new int[]{1});
        assertThat(
                this.out.toString(),
                is(
                        String.format("=== Exception : Unknown action. Enter correct value.%n%n")
                )
        );
    }

    @Test
    public void whenInputIsNumberNotInRangeThenMessage() {
        ValidateInput input = new ValidateInput(
                new StubInput(new String[]{"8", "1"})
        );
        input.ask("Enter", new int[]{1});
        assertThat(
                this.out.toString(),
                is(
                        String.format("=== Exception : Action number is out of range in menu. Enter correct value.%n%n")
                )
        );
    }
}