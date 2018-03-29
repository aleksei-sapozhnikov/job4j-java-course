package ru.job4j.count;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for CountWordsAndSpaces class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 27.03.2018
 */
public class CountWordsAndSpacesTest {

    /**
     * Not a test, demonstrating multithreading.
     */
    @Test
    public void notATest() {
        // 21 spaces, 22 words
        String input = "Hello GoodBye Мама мыла раму Ha-ha Kill me baby one more time I wanna fly like a bird Ga Ma Kill Survive";
        new CountWordsAndSpaces(input).start();
    }

    /**
     * Test countSpaces()
     */
    @Test
    public void whenWordsWithTripleSpacesThenCountThreeSpaces() {
        String input = "Hello   GoodBye";
        assertThat(new CountWordsAndSpaces(input).countSpaces(), is(3));
    }

    /**
     * Test countWords()
     */
    @Test
    public void whenSpacesInTheEndAndBeginningThenCountWordsRight() {
        String input1 = "Hello GoodBye ";
        String input2 = "Hello GoodBye   ";
        String input3 = " Hello GoodBye";
        String input4 = "   Hello GoodBye";
        String input5 = "   Hello GoodBye   ";
        assertThat(new CountWordsAndSpaces(input1).countWords(), is(2));
        assertThat(new CountWordsAndSpaces(input2).countWords(), is(2));
        assertThat(new CountWordsAndSpaces(input3).countWords(), is(2));
        assertThat(new CountWordsAndSpaces(input4).countWords(), is(2));
        assertThat(new CountWordsAndSpaces(input5).countWords(), is(2));
    }

    /**
     * Test countSpaces() and countWords()
     */
    @Test
    public void whenWordsWithSingleSpacesThenCountRightWordsAndSpaces() {
        String input = "Hello GoodBye Мама Мыла Раму";
        assertThat(new CountWordsAndSpaces(input).countSpaces(), is(4));
        assertThat(new CountWordsAndSpaces(input).countWords(), is(5));
    }
}