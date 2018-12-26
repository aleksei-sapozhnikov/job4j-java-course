package ru.job4j.interrupt;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the CountChars class.
 *
 * @author Aleksei Sapozhnikov (vermucht@gmail.com)
 * @version $Id$
 * @since 28.03.2018
 */
public class CountCharsTest {

    /**
     * Test countChars()
     */
    @Test
    public void whenInputStringThenCountCharsRight() {
        String input = "12345";
        CountChars count = new CountChars(input);
        assertThat(count.countChars(), is(5));
    }

    /**
     * Not a test, demonstrating multithreading.
     */
    @Test
    public void notATest() throws InterruptedException {
        String input = "Иван";
        Thread a = new Thread(new CountChars(input));
        a.start();
        a.join();
    }
}